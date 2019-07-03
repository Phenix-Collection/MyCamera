package com.hiar.media.recorder.audio;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

 /**
   * @author Minamo
   * @e-mail kleinminamo@gmail.com
   * @time   2019/7/3
   * @des    声音录制
   */
public class AudioRecordRecorder implements IAudioRecorder {

    private static final String TAG = "AudioRecordRecorder";

    private int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;

    /**
     * 音频采样率
     */
    public static int SAMPLE_RATE = 44100;

    /**
     * 单声道
     */
    public final static int CHANNEL = AudioFormat.CHANNEL_IN_STEREO;

    /**
     * 16比特
     */
    public final static int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    /**
     * 音频录制实例
     */
    protected AudioRecord audioRecord;

    /**
     * 录制线程
     */
    private Thread recordThread;

    /**
     * 输出的文件路径
     */
    private String pcmPath;

    /**
     * 缓冲区大小
     */
    private int bufferSize = 0;

    /**
     * 是否正在录制
     */
    private boolean isRecording = false;

    /**
     * 回调原始的PCM数据
     */
    private OnAudioRecordListener mOnAudioRecordListener;

    public AudioRecordRecorder(String filePath) {
        this.pcmPath = filePath;
    }

    @Override
    public void initRecorder() {
        if (null != audioRecord) {
            audioRecord.release();
        }
        try {
            bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL, AUDIO_FORMAT);
            bufferSize = 4096;
            audioRecord = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE, CHANNEL, AUDIO_FORMAT, bufferSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int recordStart() {
        if (isRecording) {
            return RECORD_STATE.STATE_RECORDING;
        } else if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            try {
                audioRecord.startRecording();
                isRecording = true;
                recordThread = new Thread(new AudioRecordRunnable());
                recordThread.start();
                return RECORD_STATE.STATE_SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RECORD_STATE.STATE_ERROR;
    }

    /**
     * 停止音频录制
     */
    @Override
    public void recordStop() {
        try {
            if (audioRecord != null) {
                isRecording = false;
                try {
                    if (recordThread != null) {
                        recordThread.join();
                        recordThread = null;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //释放资源
                recordRelease();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordRelease() {
        if (audioRecord != null) {
            if (audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
                audioRecord.stop();
            }
            audioRecord.release();
            audioRecord = null;
        }
    }

    private class AudioRecordRunnable implements Runnable {

        private FileOutputStream outputStream = null;

        @Override
        public void run() {
            try {
                if (!TextUtils.isEmpty(pcmPath)) {
                    outputStream = new FileOutputStream(pcmPath);
                }
                byte[] audioBuffer = new byte[bufferSize];
                while (isRecording && audioRecord != null) {
                    int audioSampleSize = audioRecord.read(audioBuffer, 0, bufferSize);
                    if (audioSampleSize > 0) {
                        if (outputStream != null) {
                            outputStream.write(audioBuffer);
                        }
                        if (mOnAudioRecordListener != null) {
                            mOnAudioRecordListener.onAudioBuffer(audioBuffer, audioBuffer.length);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(outputStream);
                outputStream = null;
            }
        }
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setOnAudioRecordListener(OnAudioRecordListener onAudioRecordListener) {
        this.mOnAudioRecordListener = onAudioRecordListener;
    }

    public interface OnAudioRecordListener {

        void onAudioBuffer(byte[] buffer, int length);

    }

}
