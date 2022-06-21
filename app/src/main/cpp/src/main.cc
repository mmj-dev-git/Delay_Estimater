#include <cstdlib>
#include <fstream>
#include <iostream>
#include <iterator>
#include <memory>
#include <vector>

#include "../cxxopts/include/cxxopts.hpp" //include by ahad
#include "apm_data_dumper.h"
#include "echo_path_delay_estimator.h"
#include "render_delay_buffer.h"
#include "wav_file.h"

#include "webrtc_delay_estimation.h"

// Compile time constants
static const constexpr char* default_num_filter = "10";
static const constexpr char* default_down_sampling_factor = "8";

static bool exists(const std::string& filename) {
  std::ifstream file_to_test(filename);
  return file_to_test.good();
}

int main() {
  using namespace webrtc_delay_estimation;

  // Path to WAV input files
    std::string render_filename = "../Wav_Files/output.wav";
    std::string capture_filename=  "../Wav_Files/outputdelayed.wav";
  // Arguments used when recognizing delay
  size_t num_filters, down_sampling_factor;

  // Whether the output should be brief
  bool verbose_output = false;


  // Make sure that the files exist first
  if (!exists(render_filename)) {
    std::cerr << render_filename << ": No such file or directory" << std::endl;
    std::exit(1);
  }
  if (!exists(capture_filename)) {
    std::cerr << capture_filename << ": No such file or directory" << std::endl;
    std::exit(1);
  }

  // Construct WAV reader from filenames provided
  webrtc::WavReader rendered(render_filename);
  webrtc::WavReader captured(capture_filename);

  // Some debug infomration about each input files

    std::cout << "Render file information:" << std::endl
              << "  sample rate: " << rendered.sample_rate() << std::endl
              << "  number of channels: " << rendered.num_channels()
              << std::endl
              << "  number of samples: " << rendered.num_samples() << std::endl
              << "Capture file information:" << std::endl
              << "  sample rate: " << captured.sample_rate() << std::endl
              << "  number of channels: " << captured.num_channels()
              << std::endl
              << "  number of samples: " << captured.num_samples() << std::endl;

  // If the files have different sampling rate or different amount of channels,
  if (rendered.sample_rate() != captured.sample_rate() ||
      rendered.num_channels() != captured.num_channels()) {
    std::cerr << "Render and capture files are incompatible. Cannot proceed."
              << std::endl;

    std::exit(5);
  }

  // Extract information from the file metadata
  auto sample_rate = rendered.sample_rate();
  auto num_channels = rendered.num_channels();

  // Read all samples into vectors
  std::vector<float> render_samples(rendered.num_samples());
  std::vector<float> capture_samples(captured.num_samples());

  rendered.ReadSamples(rendered.num_samples(), render_samples.data());
  captured.ReadSamples(captured.num_samples(), capture_samples.data());

  WavFileInfo render_info;
  render_info.num_channels = num_channels;
  render_info.sample_rate = sample_rate;
  render_info.samples = render_samples;
  WavFileInfo capture_info;
  capture_info.num_channels = num_channels;
  capture_info.sample_rate = sample_rate;
  capture_info.samples = capture_samples;

  // Generate settings
  Setting setting;
  setting.down_sampling_factor = 8;
  setting.num_filters = 10;

  try {
    auto result = EstimateDelay(render_info, capture_info, setting);

          std::cout << "Estimated delay: " << result << " sample(s) (around "
                    << result * 1000 / sample_rate << "ms)." << std::endl;
  } catch (std::exception e) {
    std::cerr << "Unable to get estimated delay value: " << e.what()
              << std::endl;
    std::exit(1);
  }

  return 0;
}
