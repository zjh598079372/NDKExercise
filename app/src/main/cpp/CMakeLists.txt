# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(FF ${CMAKE_SOURCE_DIR}/libs/armeabi-v7a)

#配置加载头文件
include_directories(include)
include_directories(include/ffmpeg)
#动态方式加载
add_library(opencv SHARED IMPORTED)
#引入第三方.so库
set_target_properties(opencv PROPERTIES IMPORTED_LOCATION ${FF}/libopencv_java3.so)

add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION ${FF}/libavcodec-57.so)

add_library(avfilter SHARED IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION ${FF}/libavfilter-6.so)

add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION ${FF}/libavformat-57.so)

add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION ${FF}/libavutil-55.so)

add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION ${FF}/libswresample-2.so)

add_library(swscale SHARED IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION ${FF}/libswscale-4.so)

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        include/Person.h
        include/Student.h
        Array.hpp
        LinkedList.hpp
        Params.cpp
        priorityQueue.hpp
        BST.hpp
        jniNativeRegister/bitmapMethod.cpp
        jniNativeRegister/bitmapMethod.h
        jniNativeRegister/nativeRegister.cpp
        jniNativeRegister/nativeRegister.h
        jniNativeRegister/playerMethod.cpp
        jniNativeRegister/playerMethod.h
        bitmap/BitmapHelper.h
        bitmap/BitmapHelper.cpp
        player/IPlayer.h
        player/IPlayer.cpp
        player/FFJniCallback.h
        player/FFJniCallback.cpp
        player/FFmpeg.cpp
        player/FFmpeg.h
        player/IPlayerPorxy.cpp
        player/IPlayerPorxy.h
        player/demux/IDemux.h
        player/demux/IDemux.cpp
        player/demux/FFDemux.h
        player/demux/FFDemux.cpp
        player/zjhMedia/ZJHMedia.h
        player/zjhMedia/ZJHMedia.cpp
        player/videoPlay/FFPlayer.h
        player/videoPlay/FFPlayer.cpp

        )

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries(
        native-lib
        opencv
        jnigraphics
        avcodec
        avfilter
        avformat
        avutil
        swresample
        swscale
        ${log-lib})
