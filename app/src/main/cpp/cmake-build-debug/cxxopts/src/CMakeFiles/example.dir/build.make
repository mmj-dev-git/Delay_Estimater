# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.16

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Remove some rules from gmake that .SUFFIXES does not remove.
SUFFIXES =

.SUFFIXES: .hpux_make_needs_suffix_list


# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E remove -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = /mnt/d/TestDelayEstimator/TestDelayEstimator

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug

# Include any dependencies generated for this target.
include cxxopts/src/CMakeFiles/example.dir/depend.make

# Include the progress variables for this target.
include cxxopts/src/CMakeFiles/example.dir/progress.make

# Include the compile flags for this target's objects.
include cxxopts/src/CMakeFiles/example.dir/flags.make

cxxopts/src/CMakeFiles/example.dir/example.cpp.o: cxxopts/src/CMakeFiles/example.dir/flags.make
cxxopts/src/CMakeFiles/example.dir/example.cpp.o: ../cxxopts/src/example.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir=/mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object cxxopts/src/CMakeFiles/example.dir/example.cpp.o"
	cd /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/src && /usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/example.dir/example.cpp.o -c /mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/src/example.cpp

cxxopts/src/CMakeFiles/example.dir/example.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/example.dir/example.cpp.i"
	cd /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/src && /usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E /mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/src/example.cpp > CMakeFiles/example.dir/example.cpp.i

cxxopts/src/CMakeFiles/example.dir/example.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/example.dir/example.cpp.s"
	cd /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/src && /usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S /mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/src/example.cpp -o CMakeFiles/example.dir/example.cpp.s

# Object files for target example
example_OBJECTS = \
"CMakeFiles/example.dir/example.cpp.o"

# External object files for target example
example_EXTERNAL_OBJECTS =

cxxopts/src/example: cxxopts/src/CMakeFiles/example.dir/example.cpp.o
cxxopts/src/example: cxxopts/src/CMakeFiles/example.dir/build.make
cxxopts/src/example: cxxopts/src/CMakeFiles/example.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir=/mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/CMakeFiles --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable example"
	cd /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/src && $(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/example.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
cxxopts/src/CMakeFiles/example.dir/build: cxxopts/src/example

.PHONY : cxxopts/src/CMakeFiles/example.dir/build

cxxopts/src/CMakeFiles/example.dir/clean:
	cd /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/src && $(CMAKE_COMMAND) -P CMakeFiles/example.dir/cmake_clean.cmake
.PHONY : cxxopts/src/CMakeFiles/example.dir/clean

cxxopts/src/CMakeFiles/example.dir/depend:
	cd /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" /mnt/d/TestDelayEstimator/TestDelayEstimator /mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/src /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/src /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/src/CMakeFiles/example.dir/DependInfo.cmake --color=$(COLOR)
.PHONY : cxxopts/src/CMakeFiles/example.dir/depend

