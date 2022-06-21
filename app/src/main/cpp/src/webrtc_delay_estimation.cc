#include "webrtc_delay_estimation.h"
#include "iostream"
#include <algorithm>
#include <iterator>
#include <android/log.h>
#include <map>

#include "apm_data_dumper.h"
#include "echo_path_delay_estimator.h"
#include "render_delay_buffer.h"
#include "map"
namespace webrtc_delay_estimation {

//const char* NoEstimateAvailableError::what() const noexcept {
//  return "The delay estimator was not able to come up with a viable delay "
//         "estimate.";
//}

//const char* IncompatibleInputsError::what() const noexcept {
//  return "The input files provided are not compatible, thus cannot estimate "
//         "delay.";
//}
    std::map<int, int> my_map;
size_t EstimateDelay(WavFileInfo& render,
                     WavFileInfo& capture,
                     Setting setting) {
  using namespace webrtc;

  // Input sanity check
//  if (render.sample_rate != capture.sample_rate ||
//      render.num_channels != capture.num_channels)
//    throw new IncompatibleInputsError();

  // Use the minimum of the samples as the base value
  size_t band_size = render.sample_rate / 16000;
  size_t num_samples = std::min(render.samples.size(), capture.samples.size());

  ApmDataDumper data_dumper(0);  // NOP data dumper

  EchoCanceller3Config config;  // configuration supplied to WebRTC algorithm
  config.delay.down_sampling_factor = setting.down_sampling_factor;
  config.delay.num_filters = setting.num_filters;

  // render buffer [band][channel][sample]
  std::vector<std::vector<std::vector<float>>> render_buffer(
      band_size, std::vector<std::vector<float>>(
                     render.num_channels, std::vector<float>(kBlockSize)));

  // capture buffer [channel][sample]
  std::vector<std::vector<float>> capture_buffer(
      capture.num_channels, std::vector<float>(kBlockSize));

  // Render delay buffer required to create downsampled render buffer
  std::unique_ptr<webrtc::RenderDelayBuffer> render_delay_buffer(
      webrtc::RenderDelayBuffer::Create(config, render.sample_rate,
                                        render.num_channels));

  // Actual estimator object
  webrtc::EchoPathDelayEstimator estimator(&data_dumper, config,
                                           capture.num_channels);

  auto render_iter = render.samples.begin();
  auto capture_iter = capture.samples.begin();

    int j=0;
  // Loop through the entire sample to find the best delay value
  absl::optional<webrtc::DelayEstimate> estimated_delay;
  for (int i = 0; i < num_samples / kBlockSize; i++) {
    auto render_end = std::next(render_iter, kBlockSize);
    auto capture_end = std::next(capture_iter, kBlockSize);

    std::copy(render_iter, render_end, render_buffer[0][0].begin());
    std::copy(capture_iter, capture_end, capture_buffer[0].begin());

    render_delay_buffer->Insert(render_buffer);

    if (i == 0)
      render_delay_buffer->Reset();

    render_delay_buffer->PrepareCaptureProcessing();

    // Try estimating the delay
    auto maybe_estimated_delay = estimator.EstimateDelay(
        render_delay_buffer->GetDownsampledRenderBuffer(), capture_buffer);

    // Sometimes, there is a new updated value, sometimes, there isn't
    if (maybe_estimated_delay) {
        estimated_delay = maybe_estimated_delay;
        my_map.insert(std::pair<int,int>(j,estimated_delay->delay));
    }
    else{
        my_map.insert(std::pair<int,int>(j,0));
    }
    // Update the iterators
    render_iter = render_end;
    capture_iter = capture_end;
      j++;
  }

  // If no estimates found, throw an error
  if (!estimated_delay){
    return 0;
  }
  else {
    return estimated_delay->delay;
  }
}
    void mapPrinter() {
        auto it = my_map.begin();
        for(int i=0;i<my_map.size(); i++ ){
            std::cout <<"MY Map elements is: " << (*it).first << " The value is: "<< (*it).second<< std::endl;
            it++;
        }
    }



    int FindIndex() {
        int currentMax =0;
        int max_index =0;
        for (auto it = my_map.cbegin(); it != my_map.cend(); ++it) {
            if (it->second > currentMax) {
                max_index = it->first;
                currentMax = it->second;
            }

        }
        __android_log_print(ANDROID_LOG_INFO, "re-allignement", "The maximux element aka delay is:  %d",currentMax);
        __android_log_print(ANDROID_LOG_INFO, "re-allignement", "The maximux element index is :  %d",max_index);
        my_map.clear();
        //        std::cout << "The maximux element is : " << currentMax << "  and it's index is: "<< max_index<< std::endl;
        return max_index * 64;
    }
}  // namespace webrtc_delay_estimation
