#! /usr/bin/python3
# -*- coding: utf-8 -*-
#-----------------------------------------------------------------------------
# Author:   Fabien Marteau <mail@fabienm.eu>
# Created:  10/12/2019
#-----------------------------------------------------------------------------
""" test_chisnespad
"""

import os
import sys
import cocotb
import logging
from cocotb.clock import Clock
from cocotb.triggers import Timer
from cocotb.result import ReturnValue
from cocotb.result import raise_error
from cocotb.binary import BinaryValue
from cocotb.triggers import RisingEdge
from cocotb.triggers import FallingEdge
from cocotb.triggers import ClockCycles


class ChisNesPadTest(object):
    """
    """
    LOGLEVEL = logging.INFO
    PERIOD = (20, "ns")
    SUPER_NES_LEN = 16
    NES_LEN = 8

    def __init__(self, dut, reg_init_value=0xcafe, reg_len=16):
        if sys.version_info[0] < 3:
            raise Exception("Must be using Python 3")
        self._dut = dut
        self._log = dut._log
        self._dut._log.setLevel(self.LOGLEVEL)
        self.reg_init_value = reg_init_value
        self._reg = reg_init_value
        self._reg_count = reg_len
        self._reg_len = reg_len
        self.clock = Clock(self._dut.clock, self.PERIOD[0], self.PERIOD[1])
        self._clock_thread = cocotb.fork(self.clock.start())
        self._register_thread = cocotb.fork(self._register())

    @cocotb.coroutine
    def reset(self):
        short_per = Timer(100, units="ns")
        self._dut.reset.value = 1
        self._dut.io_sdata.value = 0
        self._dut.io_data_ready.value = 0
        yield short_per
        self._dut.reset.value = 1
        yield short_per
        self._dut.reset.value = 0
        yield short_per

    @cocotb.coroutine
    def _register(self):

        while True:
            try: 
                dlatch = int(self._dut.io_dlatch)
            except ValueError:
                dlatch = 1
            if dlatch != 0:
                yield FallingEdge(self._dut.io_dlatch)
                self._reg = self.reg_init_value
                self._reg_count = self._reg_len
                sdata_bit = (self.reg_init_value & (0x1<<(self._reg_len-1))) >> (self._reg_len - 1)
                self._dut.io_sdata.value = sdata_bit
            else:
                sdata_bit = self._reg & (0x1<<(self._reg_len-1))
                self._dut.io_sdata.value = (sdata_bit != 0)
                if self._reg_count != 0:
                    self._reg = (self._reg << 1)
                yield [RisingEdge(self._dut.io_dclock), RisingEdge(self._dut.io_dlatch)]

@cocotb.test()#skip=True)
def simple_test(dut):
    cnpt = ChisNesPadTest(dut)
    yield cnpt.reset()
    yield Timer(1, units="us")
    dut.io_data_ready.value = 1
    yield RisingEdge(dut.io_data_valid)
    vread = int(dut.io_data_bits)
    if vread != cnpt.reg_init_value:
        msg = ("Wrong value read {:04X}, should be {:04X}"
                .format(vread, cnpt.reg_init_value))
        dut._log.error(msg)
        raise Exception(msg)
    cnpt._log.info("Value read {:04X}".format(vread))
    yield FallingEdge(dut.io_data_valid)
    dut.io_data_ready.value = 0
    yield Timer(1, units="us")

@cocotb.test()#skip=True)
def double_test(dut):
    cnpt = ChisNesPadTest(dut)
    yield cnpt.reset()
    yield Timer(1, units="us")
    dut.io_data_ready.value = 1
    yield RisingEdge(dut.io_data_valid)
    vread = int(dut.io_data_bits)
    if vread != cnpt.reg_init_value:
        msg = ("Wrong value read {:04X}, should be {:04X}"
                .format(vread, cnpt.reg_init_value))
        dut._log.error(msg)
        raise Exception(msg)
    cnpt._log.info("Value read {:04X}".format(vread))
    yield FallingEdge(dut.io_data_valid)
    dut.io_data_ready.value = 0
    yield Timer(1, units="us")

    cnpt.reg_init_value = 0xDECA
    dut.io_data_ready.value = 1
    yield RisingEdge(dut.io_data_valid)
    vread = int(dut.io_data_bits)
    if vread != cnpt.reg_init_value:
        msg = ("Wrong value read {:04X}, should be {:04X}"
                .format(vread, cnpt.reg_init_value))
        dut._log.error(msg)
        raise Exception(msg)
    cnpt._log.info("Value read {:04X}".format(vread))
    yield FallingEdge(dut.io_data_valid)
    dut.io_data_ready.value = 0
    yield Timer(1, units="us")

@cocotb.test()
def always_ready(dut):
    cnpt = ChisNesPadTest(dut)
    yield cnpt.reset()
    yield Timer(1, units="us")
    dut.io_data_ready.value = 1
    yield Timer(400, units="us")


