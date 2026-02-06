cmake_minimum_required(VERSION 3.22.1)
project("clashcore")

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

add_library(
    clashcore
    SHARED
    src/clash_jni.cpp
)

find_library(
    log-lib
    log
)

target_link_libraries(
    clashcore
    ${log-lib}
)