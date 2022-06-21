# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.16)

# Declares and names the project.

project("webrtcdelayestimator")

add_subdirectory(abseil-cpp)
add_subdirectory(src)
add_subdirectory(cxxopts)
# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
        webrtcdelayestimator

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        delay_estimator.cc
        delay_estimator.h
        checks.h
        checks.cc
        safe_compare.h
        rtc_export.h
        inline.h
        type_traits.h
        delay_estimator_wrapper.h
        delay_estimator_internal.h
        delay_estimator_wrapper.cc
        aecm_defines.h

        test_tools.h
        test_tools.cc

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

target_link_libraries( # Specifies the target library.
        webrtcdelayestimator
        absl::base
        absl::synchronization
        absl::strings
        webrtc-delay-estimation
        cxxopts
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})