#!/usr/bin/env bash
function calc() {
    return 1;
}
echo $((calc))
calc
echo $?
