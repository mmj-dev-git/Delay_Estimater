# CMake generated Testfile for 
# Source directory: /mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test
# Build directory: /mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/test
# 
# This file includes the relevant testing commands required for 
# testing this directory and lists subdirectories to be tested as well.
add_test(options "options_test")
set_tests_properties(options PROPERTIES  _BACKTRACE_TRIPLES "/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/CMakeLists.txt;4;add_test;/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/CMakeLists.txt;0;")
add_test(find-package-test "/usr/bin/ctest" "-C" "Debug" "--build-and-test" "/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/find-package-test" "/mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/test/find-package-test" "--build-generator" "Unix Makefiles" "--build-makeprogram" "/usr/bin/make" "--build-options" "-DCMAKE_CXX_COMPILER=/usr/bin/c++" "-DCMAKE_BUILD_TYPE=Debug" "-Dcxxopts_DIR=/mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts")
set_tests_properties(find-package-test PROPERTIES  _BACKTRACE_TRIPLES "/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/CMakeLists.txt;7;add_test;/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/CMakeLists.txt;0;")
add_test(add-subdirectory-test "/usr/bin/ctest" "-C" "Debug" "--build-and-test" "/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/add-subdirectory-test" "/mnt/d/TestDelayEstimator/TestDelayEstimator/cmake-build-debug/cxxopts/test/add-subdirectory-test" "--build-generator" "Unix Makefiles" "--build-makeprogram" "/usr/bin/make" "--build-options" "-DCMAKE_CXX_COMPILER=/usr/bin/c++" "-DCMAKE_BUILD_TYPE=Debug")
set_tests_properties(add-subdirectory-test PROPERTIES  _BACKTRACE_TRIPLES "/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/CMakeLists.txt;21;add_test;/mnt/d/TestDelayEstimator/TestDelayEstimator/cxxopts/test/CMakeLists.txt;0;")
