package com.fmsysj.zbqmcs.utils;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.li.videoapplication.data.local.LPDSStorageUtil;
import com.li.videoapplication.data.local.SYSJStorageUtil;

public class VideoEditor {

	public final static String TAG = VideoEditor.class.getSimpleName();

	public static String floder;

	public VideoEditor() {
		super();
		floder = SYSJStorageUtil.getSysj().getPath();
		Log.i(TAG, "floder=" + floder);
	}

	public static boolean startTrim(String src, String dst, long startMs, long endMs) {
		try {
			startTrim(new File(src), new File(dst), startMs, endMs);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void startTrim(File scr_file, File dst_file, long start_time, long end_time) throws IOException {
		Movie movie = MovieCreator.build(scr_file.getPath());
		List<Track> tracks = movie.getTracks();
		movie.setTracks(new LinkedList<Track>());
		double startTime1 = start_time / 1000;
		double endTime1 = end_time / 1000;
		boolean timeCorrected = false;
		// Here we try to find a track that has sync samples. Since we can only
		// startCollectionActivity decoding
		// at such a sample we SHOULD make sure that the startCollectionActivity of the new
		// fragment is exactly
		// such a frame
		for (Track track : tracks) {
			if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
				if (timeCorrected) {
					throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
				}
				startTime1 = correctTimeToSyncSample(track, startTime1, false);
				endTime1 = correctTimeToSyncSample(track, endTime1, true);
				timeCorrected = true;
			}
		}
		for (Track track : tracks) {
			long currentSample = 0;
			double currentTime = 0;
			double lastTime = 0;
			long startSample1 = -1;
			long endSample1 = -1;

			for (int i = 0; i < track.getSampleDurations().length; i++) {
				long delta = track.getSampleDurations()[i];
				if (currentTime <= startTime1) {
					startSample1 = currentSample;
				}
				if (currentTime <= endTime1) {
					endSample1 = currentSample;
				}
				lastTime = currentTime;
				currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
				currentSample++;
			}
			movie.addTrack(new CroppedTrack(track, startSample1, endSample1));
			// movie.addTrack(new AppendTrack(new CroppedTrack(track,
			// startSample1, endSample1),new CroppedTrack(track, startSample1,
			// endSample1)));
		}
		Container out = new DefaultMp4Builder().build(movie);
		FileOutputStream fos = new FileOutputStream(dst_file);
		FileChannel fc = fos.getChannel();
		out.writeContainer(fc);
		fc.close();
		fos.close();
	}

	private static double correctTimeToSyncSample(Track track, double cutHere, boolean next) {
		double[] timeOfSyncSamples = new double[track.getSyncSamples().length];
		long currentSample = 0;
		double currentTime = 0;
		for (int i = 0; i < track.getSampleDurations().length; i++) {
			long delta = track.getSampleDurations()[i];

			if (Arrays.binarySearch(track.getSyncSamples(), currentSample + 1) >= 0) {
				// samples always startCollectionActivity with 1 but we startCollectionActivity with zero therefore
				// +1
				timeOfSyncSamples[Arrays.binarySearch(track.getSyncSamples(), currentSample + 1)] = currentTime;
			}
			currentTime += (double) delta / (double) track.getTrackMetaData().getTimescale();
			currentSample++;
		}
		double previous = 0;
		for (double timeOfSyncSample : timeOfSyncSamples) {
			if (timeOfSyncSample > cutHere) {
				if (next) {
					return timeOfSyncSample;
				} else {
					return previous;
				}
			}
			previous = timeOfSyncSample;
		}
		return timeOfSyncSamples[timeOfSyncSamples.length - 1];
	}

	public static String appendVideo(String[] videos) {
		String appendVideoName;
		try {
			Movie[] inMovies = new Movie[videos.length];
			int index = 0;
			for (String video : videos) {
				inMovies[index] = MovieCreator.build(video);
				index++;
			}
			List<Track> videoTracks = new LinkedList<>();
			List<Track> audioTracks = new LinkedList<>();
			for (Movie m : inMovies) {
				for (Track t : m.getTracks()) {
					if (t.getHandler().equals("soun")) {
						audioTracks.add(t);
					}
					if (t.getHandler().equals("vide")) {
						videoTracks.add(t);
					}
				}
			}
			Movie result = new Movie();
			if (audioTracks.size() > 0) {
				result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
			}
			if (videoTracks.size() > 0) {
				result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
			}
			Container out = new DefaultMp4Builder().build(result);
			appendVideoName = floder + File.separator + "temp" + File.separator + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + ".mp4";
			FileChannel fc = new RandomAccessFile(String.format(appendVideoName), "rw").getChannel();
			out.writeContainer(fc);
			fc.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return appendVideoName;
	}

	public static String MuxVideoAndAudio(String video, String audioEnglish) {
		String outPutFile = floder + File.separator + new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss").format(new Date()) + ".mp4";
		try {
			// String audioEnglish =
			// MainActivity.floder+File.separator+"temp"+File.separator+"audio.mp4";
			// String video =
			// MainActivity.floder+File.separator+"temp"+File.separator+"output_append.mp4";
			Movie countVideo = MovieCreator.build(video);
			Movie countAudioEnglish = MovieCreator.build(audioEnglish);
			Track audioTrackEnglish = countAudioEnglish.getTracks().get(0);
			countVideo.addTrack(audioTrackEnglish);
			Container out = new DefaultMp4Builder().build(countVideo);
			FileOutputStream fos = new FileOutputStream(new File(outPutFile));
			out.writeContainer(fos.getChannel());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return outPutFile;
	}

}