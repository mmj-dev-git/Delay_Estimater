#include <jni.h>
#include <string>

#include <cstdlib>
#include <fstream>
#include <iostream>
#include <iterator>
#include <memory>
#include <vector>

#include "src/apm_data_dumper.h"
#include "src/echo_path_delay_estimator.h"
#include "src/render_delay_buffer.h"
#include "src/wav_file.h"

#include "src/webrtc_delay_estimation.h"
#include "android/log.h"
#include "src/audio_util.h"

// Compile time constants
static const constexpr char* default_num_filter = "10";
static const constexpr char* default_down_sampling_factor = "8";
int chunk_size_to_process = 144000;

std::vector<float> render_samples_realtime;
std::vector<float> capture_samples_realtime;

std::vector<float> render_buffer_re_aligned;
std::vector<float> capture_buffer_re_aligned;


size_t print_iter_render =0;
size_t print_iter_capture =0;
size_t print_iter_capture_original =0;

//std::vector<float> render_samples_chunk(chunk_size_to_process);
//std::vector<float> capture_samples_chunk(chunk_size_to_process);
std::vector<float> render_samples_chunk;
std::vector<float> capture_samples_chunk;
//float arra[1000] = {0.0};
int samples_nearEnd = 0;
int samples_farEnd =0;
static bool exists(const std::string& filename) {
    std::ifstream file_to_test(filename);
    return file_to_test.good();
}

//void ProcessEstimateDelay();

//Java_com_example_webrtcdelayestimator_MainActivity_stringFromJNI(

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_webrtcdelayestimator_MainClass_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {

//    ProcessEstimateDelay(nullptr);
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

void fillNearEndBuff(float *inputArrayNearEnd){
        capture_samples_realtime.insert(capture_samples_realtime.end(), inputArrayNearEnd, inputArrayNearEnd +1024);

}

void fillFarEndBuff(float *inputArrayFarEnd){

        render_samples_realtime.insert(render_samples_realtime.end(), inputArrayFarEnd, inputArrayFarEnd + 1024);
}



void ProcessEstimateDelay() {
    using namespace webrtc_delay_estimation;


// ******** code changes for reading data in chunks **********************

    auto my_render_iter = render_samples_realtime.begin();
    auto my_capture_iter = capture_samples_realtime.begin();
    __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "1");
    auto min_size = std::min(render_samples_realtime.size(), capture_samples_realtime.size());
    __android_log_print(ANDROID_LOG_ERROR, "MyTag", "Iterations that should be completed: %d", min_size/chunk_size_to_process);
    for(int i=0; i<min_size/chunk_size_to_process; i++){
        __android_log_print(ANDROID_LOG_ERROR, "MyTag", "Iteration begins here");
//        auto my_render_end = std::next(my_render_iter, chunk_size_to_process);
//        auto my_capture_end = std::next(my_capture_iter, chunk_size_to_process);
        auto my_render_end = my_render_iter + chunk_size_to_process;
        auto my_capture_end = my_capture_iter + chunk_size_to_process;


        try {
            __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "2");
//            std::copy(my_render_iter, my_render_end, render_samples_chunk.begin());
            render_samples_chunk.insert(render_samples_chunk.end(), render_samples_realtime.begin() , render_samples_realtime.begin() + chunk_size_to_process);
            __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "3");
            capture_samples_chunk.insert(capture_samples_chunk.end(), capture_samples_realtime.begin() , capture_samples_realtime.begin()+chunk_size_to_process);
//            std::copy(my_capture_iter, my_capture_end, capture_samples_chunk.begin());

        }
        catch (std::exception e){

        }
        __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "4");
//        for(int i=0 ; i<chunk_size_to_process ; i++){
//        render_samples_chunk[i] =webrtc::FloatToFloatS16(render_samples_chunk[i]);
//        capture_samples_chunk[i] = webrtc::FloatToFloatS16(capture_samples_chunk[i]);
//    }

        WavFileInfo render_info;
        render_info.num_channels = 1;
        render_info.sample_rate = 48000;
        render_info.samples = render_samples_chunk;
        __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "5");
        WavFileInfo capture_info;
        capture_info.num_channels = 1;
        capture_info.sample_rate = 48000;
        capture_info.samples = capture_samples_chunk;
        __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "6");

        // Generate settings
        Setting setting;
        setting.down_sampling_factor = 8;
        setting.num_filters = 10;
        size_t result;
        int delayed_index = 0;
        try {
            result = EstimateDelay(render_info, capture_info, setting);
            __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "7");
            __android_log_print(ANDROID_LOG_INFO, "MyTag", "Estimated delay samples are:  %d",
                                result);
            delayed_index =  FindIndex();
            __android_log_print(ANDROID_LOG_INFO, "re-allignement", " Delay_Index is: %d",delayed_index);
            __android_log_print(ANDROID_LOG_INFO, "MyTag", "Estimated delay in ms:  %d",
                                result * 1000 / 48000);

        } catch (std::exception e) {
            __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "8");
            __android_log_print(ANDROID_LOG_ERROR, "MyTag", "Unable to get the estimated delay");

        }


        // Update the iterators
        my_render_iter = my_render_end;
        my_capture_iter = my_capture_end;


//        render_buffer_re_aligned.insert(render_buffer_re_aligned.end(), render_samples_realtime.begin() , render_samples_realtime.begin() + chunk_size_to_process);
//        capture_buffer_re_aligned.insert(capture_buffer_re_aligned.end(),capture_samples_realtime.begin() + result,capture_samples_realtime.begin()+ chunk_size_to_process);
//
        // for maintaining buffer which doesn't have delay improved logic
        if( result!= 0) {
            render_buffer_re_aligned.insert(render_buffer_re_aligned.end(),
                                            render_samples_realtime.begin() + delayed_index,
                                            render_samples_realtime.begin() +
                                            chunk_size_to_process);
            capture_buffer_re_aligned.insert(capture_buffer_re_aligned.end(),
                                             capture_samples_realtime.begin() + delayed_index +
                                             result, capture_samples_realtime.begin() +
                                                     chunk_size_to_process);
        }





        // ************* for printing the aligned buffers ***************************************

        std::string strr4;
        for(auto it = 0;it< render_samples_chunk.size(); it++)
        {
            strr4 += std::to_string(render_samples_chunk[it]) + ", ";

        }
        __android_log_print(ANDROID_LOG_INFO, "re-allignement", "render samples original: %s",strr4.c_str());


        std::string strr1;
        for(auto it = print_iter_render;it< render_buffer_re_aligned.size(); it++)
        {
            strr1 += std::to_string(render_buffer_re_aligned[it]) + ", ";

        }
        print_iter_render += chunk_size_to_process;
//        print_iter_render = render_buffer_re_aligned.size();
        __android_log_print(ANDROID_LOG_ERROR, "re-allignement", "Render Buffer Re_aligned: %s",strr1.c_str());

        __android_log_print(ANDROID_LOG_INFO, "re-allignement", "samples delay is: %zu",result);


        std::string strr3;
        for(auto it = 0;it< capture_samples_chunk.size(); it++)
        {
            strr3 += std::to_string(capture_samples_chunk[it]) + ", ";

        }
        __android_log_print(ANDROID_LOG_INFO, "re-allignement", "Capture samples original: %s",strr3.c_str());


        std::string strr2;


        for(auto it = print_iter_capture;it< capture_buffer_re_aligned.size(); it++)
        {
            strr2 += std::to_string(capture_buffer_re_aligned[it]) + ", ";

        }
//        print_iter_capture = (print_iter_capture + (chunk_size_to_process - result));
        print_iter_capture = capture_buffer_re_aligned.size();

        __android_log_print(ANDROID_LOG_ERROR, "re-allignement", "Capture Buffer Re_aligned: %s",strr2.c_str());
        __android_log_print(ANDROID_LOG_ERROR, "re-allignement", "*************** Next Iteration *********************");


        // ****************************************************************************************
        render_samples_realtime.erase( render_samples_realtime.begin(), render_samples_realtime.begin() + chunk_size_to_process);
        capture_samples_realtime.erase(capture_samples_realtime.begin(),capture_samples_realtime.begin() + chunk_size_to_process);
//        render_samples_chunk.erase(render_samples_chunk.begin(),render_samples_chunk.end());
//        capture_samples_chunk.erase(capture_samples_chunk.begin(),capture_samples_chunk.end());
        render_samples_chunk.clear();
        capture_samples_chunk.clear();

        __android_log_print(ANDROID_LOG_ERROR, "CrashTest", "9");
    }
}

// *******************************************************


// ****************** For complete file **************


//    for(int i=0 ; i<1024 ; i++){
//        render_samples_realtime[i] =webrtc::FloatToFloatS16(render_samples_realtime[i]);
//        capture_samples_realtime[i] = webrtc::FloatToFloatS16(capture_samples_realtime[i]);
//    }
//
//
//    WavFileInfo render_info;
//    render_info.num_channels = 1;
//    render_info.sample_rate = 48000;
//    render_info.samples = render_samples_realtime;
//    WavFileInfo capture_info;
//    capture_info.num_channels = 1;
//    capture_info.sample_rate = 48000;
//    capture_info.samples = capture_samples_realtime;
//
//    // Generate settings
//    Setting setting;
//    setting.down_sampling_factor = 8;
//    setting.num_filters = 10;
//    try {
//        __android_log_print(ANDROID_LOG_INFO, "MyTag", "Before calling function");
//        auto result = EstimateDelay(render_info, capture_info, setting);
//
//
//        __android_log_print(ANDROID_LOG_INFO, "MyTag", "Estimated delay samples are:  %d", result);
//        __android_log_print(ANDROID_LOG_INFO, "MyTag", "Estimated delay in ms:  %d", result * 1000 / 48000 );
//
//    } catch (std::exception e) {
//        __android_log_print(ANDROID_LOG_ERROR, "MyTag", "Unable to get the estimated delay");
//
//    }
//}
// **********************************************

//}



extern "C"
JNIEXPORT void JNICALL
Java_com_example_webrtcdelayestimator_MainClass_fillNearEndBuffer(JNIEnv *env, jobject thiz,
                                                                  jfloatArray recorded_data) {
    jfloat *FloatRecordingArray = env->GetFloatArrayElements(recorded_data,0);
//    std::string strr5;
//    for(auto it =0 ; it < 1024; it++)
//    {
//        strr5 += std::to_string(FloatRecordingArray[it]) + ", ";
//    }
//    __android_log_print(ANDROID_LOG_ERROR, "JNI_data:", "Near End DATA : %s",strr5.c_str());

    fillNearEndBuff(FloatRecordingArray);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_webrtcdelayestimator_MainClass_fillFarEndBuffer(JNIEnv *env, jobject thiz,
                                                                 jfloatArray playing_data) {
    // TODO: implement fillFarEndBuffer()
    jfloat *FloatPlayingArray = env->GetFloatArrayElements(playing_data,0);

//    std::string strr6;
//    for(auto it =0 ; it < 1024; it++)
//    {
//        strr6 += std::to_string(FloatPlayingArray[it]) + ", ";
//    }
//    __android_log_print(ANDROID_LOG_ERROR, "JNI_data:", "Far End DATA : %s",strr6.c_str());

    fillFarEndBuff(FloatPlayingArray);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_webrtcdelayestimator_MainClass_processData(JNIEnv *env, jobject thiz) {
    // TODO: implement processData()
    ProcessEstimateDelay();
}