Este software utiliza bibliotecas open source conforme listado em NOTICE.txt e LICENSES.txt.

Executar para mostrar video:

gst-launch-1.0 rtspsrc location="rtsp://admin:lunaduna7@192.168.15.21:554/cam/realmonitor?channel=1&subtype=0" latency=100 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:S4e$2025$@192.168.1.64:554/Streaming/Channels/101" latency=100 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:lunaduna7@192.168.15.21:554/cam/realmonitor?channel=1&subtype=0" protocols=tcp latency=10 name=CAM-5source CAM-5source. ! queue max-size-buffers=20 max-size-bytes=10485760 max-size-time=0 ! rtph264depay ! queue ! h264parse ! queue ! avdec_h264 ! queue ! identity silent=true ! watchdog timeout=3000 ! queue ! videoflip video-direction=0 ! videoflip method=none ! queue ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:asdf1234@192.168.15.24:554/cam/realmonitor?channel=1&subtype=0" latency=100 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:asdf1234@192.168.15.24/cam/realmonitor?channel=1&subtype=0" latency=10 ! rtph264depay ! h264parse ! d3d11h264dec ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:ATERPA-ADMIN@192.168.60.251:554/cam/realmonitor?channel=1&subtype=0" latency=100 ! rtph265depay ! h265parse ! avdec_h265 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:lunaduna07@192.168.99.111:554/cam/realmonitor?channel=1&subtype=0" latency=100 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location=rtsp://admin:lunaduna07@192.168.15.27:554 latency=100 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink 

gst-launch-1.0 rtspsrc location="rtsp://admin:123@192.168.15.88:554/cam3/mainstream" protocols=tcp latency=10 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:123@192.168.15.88:554/cam3/mainstream" protocols=tcp latency=10 ! rtph264depay ! h264parse ! d3d11h264dec ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

gst-launch-1.0 rtspsrc location="rtsp://admin:asdf1234@192.168.15.88:554/cam/realmonitor?channel=1&subtype=0" latency=100 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink

Executar para lagging:
gst-launch-1.0 rtspsrc location="rtsp://admin:asdf123@192.168.15.24:554" protocols=4 latency=50 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! queue leaky=downstream max-size-buffers=10 max-size-time=0 ! autovideosink sync=false

Executar para gravar video:
gst-launch-1.0 rtspsrc location=rtsp://admin:lunaduna07@192.168.15.27:554 latency=100 ! rtph264depay ! h264parse ! tee name=t ! queue ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink t. ! queue ! matroskamux ! filesink location=output.mkv

Executar para gravar a tela:
 gst-launch-1.0 d3d11screencapturesrc monitor-index=0 ! videoconvert ! videoscale ! video/x-raw,width=1366,height=768 ! x264enc bitrate=1000 ! matroskamux ! filesink location=C:/s4etech-viewer/gravacoes/scicoti/171223/monitor0/115949.mp4
  
 Executar para mostrar o fsp:
 gst-launch-1.0  rtspsrc location=rtsp://admin:lunaduna07@192.168.15.28:554 latency=100 ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 !  fpsdisplaysink video-sink=autovideosink sync=true text-overlay=true

Executar para mostrar video/audio:
gst-launch-1.0 rtspsrc location=rtsp://admin:lunaduna07@192.168.15.119:554 latency=100 name=source source. ! queue ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink source. ! queue ! rtpg726depay ! avdec_g726 ! audioconvert ! audioresample ! autoaudiosink

gst-launch-1.0 rtspsrc location=rtsp://admin:lunaduna7@192.168.15.24:554 latency=100 name=source source. ! queue ! rtph264depay ! h264parse ! avdec_h264 ! videoconvert ! videoscale ! video/x-raw,width=640,height=360 ! autovideosink source. ! queue ! rtpg726depay ! avdec_g726 ! audioconvert ! audioresample ! autoaudiosink

Executar somente o audio:
gst-launch-1.0 rtspsrc location=rtsp://admin:lunaduna7@192.168.15.24:554 latency=100 ! rtpg726depay ! avdec_g726 ! audioconvert ! audioresample ! autoaudiosink

Converter WAV to PCM
gst-launch-1.0 filesrc location=file.wav ! wavparse ! audioresample ! audioconvert ! audio/x-raw,format=S16BE,channels=1,rate=8000 ! filesink location=file.pcm
PLAY PCM
gst-launch-1.0 filesrc location=file.pcm ! audio/x-raw,format=S16BE,channels=1,rate=8000 ! audioconvert ! audioresample ! autoaudiosink

Esse audio funcionou para arquivo.
gst-launch-1.0 filesrc location="file.pcm" ! "audio/x-raw,format=(string)S16BE,rate=(int)8000,channels=(int)1,layout=(string)interleaved" ! audioconvert ! audioresample ! autoaudiosink

Esse audio funcionou para a camera do dvr.
gst-launch-1.0 rtspsrc location=rtsp://admin:admin@192.168.15.88:554/cam3/mainstream?Audio=true ! rtpmp4gdepay ! aacparse ! mfaacdec ! audioconvert ! audioresample ! autoaudiosink

gst-launch-1.0 rtspsrc location=rtsp://admin:admin@192.168.15.88:554/cam4/mainstream?Audio=true ! rtpmp4gdepay ! aacparse ! mfaacdec ! audioconvert ! audioresample ! autoaudiosink


    gstreamer1.0-tools
    gstreamer1.0-plugins-base
    gstreamer1.0-plugins-good
    gstreamer1.0-plugins-bad
    gstreamer1.0-plugins-ugly
    gstreamer1.0-libav
