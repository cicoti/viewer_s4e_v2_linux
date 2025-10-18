package com.s4etech.util;
import org.freedesktop.gstreamer.Bus;
import org.freedesktop.gstreamer.Gst;
import org.freedesktop.gstreamer.Pipeline;
import org.freedesktop.gstreamer.State;
import org.freedesktop.gstreamer.message.StateChangedMessage;

public class ValidaRTSP {

    private Pipeline pipeline;
    private boolean isPlaying = false;
    private boolean initialized = false;

    public ValidaRTSP(String rtspUrl) {
        Gst.init("ValidaRTSP");
        String pipelineDesc = "rtspsrc location=" + rtspUrl + " !  fakesink";
        pipeline = (Pipeline) Gst.parseLaunch(pipelineDesc);

        Bus bus = pipeline.getBus();
        bus.connect((Bus.MESSAGE) (source, message) -> {
            if (message instanceof StateChangedMessage) {
                StateChangedMessage stateMessage = (StateChangedMessage) message;
                if (stateMessage.getSource() == pipeline) {
                    if (stateMessage.getNewState() == State.PLAYING) {
                        isPlaying = true;
                        initialized = true;
                    }
                }
            }
        });
    }

    public String testVideoStream() {
        if (pipeline != null) {
            pipeline.play();
            waitForPipeline(6000);  // Wait for 6 seconds
            if (isPlaying) {
                return "sucesso";
            } else {
                return null;
            }
        }
        return "Pipeline not initialized";
    }

    private void waitForPipeline(int timeout) {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < timeout && !initialized) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stop() {
        if (pipeline != null) {
            pipeline.stop();
            //System.out.println("Pipeline has been stopped.");
        }
    }

    public void release() {
        if (pipeline != null) {
            pipeline.setState(State.NULL);
            pipeline.dispose();
            //System.out.println("Pipeline has been released.");
        }
    }

    /*
    public static void main(String[] args) {
    	String rtspUrl = "rtsp://192.168.15.88:554/cam1/mainstream";
        ValidaRTSP player = new ValidaRTSP(rtspUrl);
        System.out.println(player.testVideoStream());
        player.stop();
        player.release();
        rtspUrl = "rtsp://192.168.15.88:554/cam3/mainstream";
        player = new ValidaRTSP(rtspUrl);
        System.out.println(player.testVideoStream());
        player.stop();
        player.release();
        rtspUrl = "rtsp://admin:lunaduna07@192.168.15.28:554";
        player = new ValidaRTSP(rtspUrl);
        System.out.println(player.testVideoStream());
        player.stop();
        player.release();
        rtspUrl = "rtsp://admin:lunaduna07@192.168.15.27:554";
        player = new ValidaRTSP(rtspUrl);
        System.out.println(player.testVideoStream());
        player.stop();
        player.release();
        Gst.deinit();
    }
    */
}
