# Export WebRTC portion as a library

#set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -DWEBRTC_APM_DEBUG_DUMP=1")
#set (CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -DWEBRTC_APM_DEBUG_DUMP=0")


#/////////////////////settings from rangho///////////////////////////////////
set (CMAKE_CXX_STANDARD 14)
set (CMAKE_CXX_STANDARD_REQUIRED true)

set (CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -DWEBRTC_APM_DEBUG_DUMP=1")
set (CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE} -DWEBRTC_APM_DEBUG_DUMP=0")



# Platform-specific build arguments
#if (${CMAKE_SYSTEM_NAME} MATCHES "Windows")
#    add_compile_definitions (
#            "WEBRTC_WIN"
#            "USE_AURA=1"
#            "CR_CLANG_REVISION=\"llvmorg-12-init-12923-g6ee22ca6-1\""
#            "_HAS_NODISCARD"
#            "_LIBCPP_ABI_UNSTABLE"
#            "_LIBCPP_DISABLE_VISIBILITY_ANNOTATIONS"
#            "_LIBCPP_ENABLE_NODISCARD"
#            "_LIBCPP_DEBUG=0"
#            "_LIBCPP_NO_AUTO_LINK"
#            "__STD_C"
#            "_CRT_RAND_S"
#            "_CRT_SECURE_NO_DEPRECATE"
#            "_SCL_SECURE_NO_DEPRECATE"
#            "_ATL_NO_OPENGL"
#            "_WINDOWS"
#            "CERT_CHAIN_PARA_HAS_EXTRA_FIELDS"
#            "PSAPI_VERSION=2"
#            "WIN32"
#            "_SECURE_ATL"
#            "WINAPI_FAMILY=WINAPI_FAMILY_DESKTOP_APP"
#            "WIN32_LEAN_AND_MEAN"
#            "NOMINMAX"
#            "_UNICODE"
#            "UNICODE"
#            "NTDDI_VERSION=NTDDI_WIN10_VB"
#            "_WIN32_WINNT=0x0A00"
#            "WINVER=0x0A00"
#            "_DEBUG"
#            "DYNAMIC_ANNOTATIONS_ENABLED=1"
#            "WEBRTC_ENABLE_PROTOBUF=1"
#            "WEBRTC_INCLUDE_INTERNAL_AUDIO_DEVICE"
#            "RTC_ENABLE_VP9"
#            "HAVE_SCTP"
#            "WEBRTC_LIBRARY_IMPL"
#            "WEBRTC_ENABLE_AVX2"
#            "WEBRTC_NON_STATIC_TRACE_EVENT_HANDLERS=1"
#            "WEBRTC_WIN"
#            "ABSL_ALLOCATOR_NOTHROW=1"
#    )
#elseif (${CMAKE_SYSTEM_NAME} MATCHES "Darwin")
#    add_compile_definitions (
#            "WEBRTC_POSIX"
#            "WEBRTC_MAC"
#    )
#elseif (${CMAKE_SYSTEM_NAME} MATCHES "Linux")
#    add_compile_definitions (
#            "WEBRTC_POSIX"
#            "WEBRTC_LINUX"
#    )
#    add_compile_options(-march=native)
#else ()
#    message (FATAL_ERROR "Unsupported platform detected!")
#endif ()

add_compile_definitions (
                    "WEBRTC_POSIX"
                    "WEBRTC_LINUX"
            )

# ///////////////////////////////////////////////////////////////////////////




add_library (webrtc-delay-estimation

    "webrtc_delay_estimation.cc"

    # api/
    "arraysize.h"

    # api/audio/
    "echo_canceller3_config.cc"
    "echo_canceller3_config.h"

    # common_audio/
    "wav_file.cc"
    "wav_file.h"
    "wav_header.cc"
    "wav_header.h"

    # common_audio/include/
    "audio_util.h"

    # common_audio/third_party/ooura/fft_size_128
    "ooura_fft.cc"
    "ooura_fft.h"
#    "ooura_fft_sse2.cc"
    "ooura_fft_tables_common.h"
    "ooura_fft_tables_neon_sse2.h"
    
    # modules/audio_processing/aec3/
    "aec3_common.cc"
    "aec3_common.h"
    "aec3_fft.cc"
    "aec3_fft.h"
    "alignment_mixer.cc"
    "alignment_mixer.h"
    "block_buffer.cc"
    "block_buffer.h"
    "clockdrift_detector.cc"
    "clockdrift_detector.h"
    "decimator.cc"
    "decimator.h"
    "delay_estimate.h"
    "downsampled_render_buffer.cc"
    "downsampled_render_buffer.h"
    "echo_path_delay_estimator.cc"
    "echo_path_delay_estimator.h"
    "fft_buffer.cc"
    "fft_buffer.h"
    "fft_data.h"
#    "fft_data_avx2.cc"
    "matched_filter.cc"
    "matched_filter.h"
#    "matched_filter_avx2.cc"
    "matched_filter_lag_aggregator.cc"
    "matched_filter_lag_aggregator.h"
    "render_buffer.cc"
    "render_buffer.h"
    "render_delay_buffer.cc"
    "render_delay_buffer.h"
    "spectrum_buffer.cc"
    "spectrum_buffer.h"

    # modules/audio_processing/logging/
    "apm_data_dumper.cc"
    "apm_data_dumper.h"

    # modules/audio_processing/utility/
    "cascaded_biquad_filter.cc"
    "cascaded_biquad_filter.h"

    # rtc_base/
    "array_view.h"
    "atomic_ops.h"
    "checks.cc"
    "checks.h"
    "constructor_magic.h"
    "deprecation.h"
    "logging.cc"
    "logging.h"
    "platform_thread_types.cc"
    "platform_thread_types.h"
    "sanitizer.h"
    "string_utils.cc"
    "string_utils.h"
    "thread_annotations.h"
    "time_utils.cc"
    "time_utils.h"
    "type_traits.h"

    # rtc_base/numerics/
    "safe_compare.h"
    "safe_conversions.h"
    "safe_conversions_impl.h"
    "safe_minmax.h"

    # rtc_base/strings/
    "string_builder.cc"
    "string_builder.h"
    "string_encode.cc"
    "string_encode.h"
    "string_to_number.cc"
    "string_to_number.h"

    # rtc_base/synchronization/
    "mutex.cc"
    "mutex.h"
    "mutex_abseil.h"
    "mutex_critical_section.h"
    "mutex_pthread.h"
    "yield.cc"
    "yield.h"

    # rtc_base/system/
    "arch.h"
    "file_wrapper.cc"
    "file_wrapper.h"
    "inline.h"
    "rtc_export.h"
    "unused.h"

    # system_wrappers/include/
    "cpu_features_wrapper.h"
    "field_trial.h"

    # system_wrappers/source/
    "cpu_features.cc"
    "field_trial.cc"
)

# Platform-specific source code
if (${CMAKE_SYSTEM_NAME} MATCHES "Linux")
    target_sources (webrtc-delay-estimation PRIVATE "cpu_features_linux.cc")
endif ()

# Add main library dependencies
target_include_directories (webrtc-delay-estimation PUBLIC
    "${PROJECT_SOURCE_DIR}/include"
)
add_dependencies (webrtc-delay-estimation
    absl::strings
)
target_link_libraries (webrtc-delay-estimation PRIVATE
    absl::strings
)

if (${CMAKE_SYSTEM_NAME} MATCHES "Windows")
    # Windows Multimedia Library is required for Windows
    target_link_libraries (webrtc-delay-estimation PRIVATE winmm)
elseif (${CMAKE_SYSTEM_NAME} MATCHES "Linux")
    # pthread library is required for Linux
    target_link_libraries (webrtc-delay-estimation PRIVATE pthread)
endif ()

# Standalone executable build
add_executable (delay-estimator
    "main.cc"
)

# Add executable dependencies
add_dependencies (delay-estimator
    webrtc-delay-estimation

    absl::strings
    cxxopts
)
target_link_libraries(delay-estimator PRIVATE
    webrtc-delay-estimation

    absl::strings
    cxxopts
)

