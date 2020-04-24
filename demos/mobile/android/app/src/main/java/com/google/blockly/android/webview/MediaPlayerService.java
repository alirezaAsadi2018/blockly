package com.google.blockly.android.webview;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.blockly.android.webview.utility.Codes;
import com.google.blockly.android.webview.utility.Track;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaPlayerService extends Service implements Codes {
    public static final String CHANNEL_ID = "channel1";
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";
    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";
    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";
    public static final String ACTION_STOP = "action_stop";

    private MediaPlayer mMediaPlayer;
    private NotificationManagerCompat mManager;
    private MediaSession mSession;
    private MediaController mController;
    private int mSeekValue = 1000; // milliSeconds
    private int mCurrentIndex;
    private List<Track> mTrackList;
    private String currentMediaTitle = "Media Title";
    private String currentMediaArtist = "Media Artist";

    public MediaPlayerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (action.equalsIgnoreCase(ACTION_PLAY)) {
            mController.getTransportControls().play();
        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {
            mController.getTransportControls().pause();
        } else if (action.equalsIgnoreCase(ACTION_FAST_FORWARD)) {
            mController.getTransportControls().fastForward();
        } else if (action.equalsIgnoreCase(ACTION_REWIND)) {
            mController.getTransportControls().rewind();
        } else if (action.equalsIgnoreCase(ACTION_PREVIOUS)) {
            mController.getTransportControls().skipToPrevious();
        } else if (action.equalsIgnoreCase(ACTION_NEXT)) {
            mController.getTransportControls().skipToNext();
        } else if (action.equalsIgnoreCase(ACTION_STOP)) {
            mController.getTransportControls().stop();
        }
    }

    private NotificationCompat.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                MUSIC_PLAYER_NOTIFICATION_ID, intent, 0);
        return new NotificationCompat.Action.Builder(icon, title, pendingIntent).build();
//        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }


    private void buildNotification(NotificationCompat.Action action) {
        Notification.MediaStyle style = new Notification.MediaStyle();
        mManager = NotificationManagerCompat.from(getApplicationContext());
        MediaSession mediaSession = new MediaSession(getApplicationContext(),
                "simple player session");
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.mipmap.roobin);
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                1, intent, 0);


//        NotificationCompat.Builder builder = new NotificationCompat.
//                Builder(getApplicationContext(), CHANNEL_ID);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),
                CHANNEL_ID)
                .setOngoing(true)
                .setLargeIcon(icon) // album artwork icon
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(currentMediaTitle)
                .setContentText(currentMediaArtist)
                .setDeleteIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(
                        MediaSessionCompat.Token.fromToken(mediaSession.getSessionToken())));

//                .setStyle(new Notification.MediaStyle().setMediaSession(
//                        mediaSession.getSessionToken()));

        builder.addAction(generateAction(android.R.drawable.ic_media_previous,
                "Previous", ACTION_PREVIOUS));
        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_ff,
                "Fast Foward", ACTION_FAST_FORWARD));
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
        style.setShowActionsInCompactView(0, 1, 2, 3, 4);

        mManager.notify(MUSIC_PLAYER_NOTIFICATION_ID, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mManager == null) {
            mTrackList = new ArrayList<>();
            mTrackList.add(new Track("azzy_frenchy", "Benjamin Tissot"));
            mTrackList.add(new Track("summer", "Benjamin Tissot"));
            mTrackList.add(new Track("ukulele", "Benjamin Tissot"));
            mTrackList.add(new Track("happy_rock", "Benjamin Tissot"));
            mCurrentIndex = new SecureRandom().nextInt(mTrackList.size());
            currentMediaTitle = mTrackList.get(mCurrentIndex).getTitle();
            currentMediaArtist = mTrackList.get(mCurrentIndex).getArtist();
            initMediaSessions();
        }
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSessions() {
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(
                mTrackList.get(mCurrentIndex).getTitle(), "raw", getPackageName()));

        mMediaPlayer.setOnCompletionListener(mp -> {
            mCurrentIndex++;
            mCurrentIndex %= mTrackList.size();
            skipToIndex();
        });

        mSession = new MediaSession(getApplicationContext(), "simple player session");
        mController = new MediaController(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback() {
                                 @Override
                                 public void onPlay() {
                                     super.onPlay();
                                     Log.e("MediaPlayerService", "onPlay");
                                     buildNotification(generateAction(android.R.drawable.
                                             ic_media_pause, "Pause", ACTION_PAUSE));
                                     if (!mMediaPlayer.isPlaying())
                                         mMediaPlayer.start();
                                 }

                                 @Override
                                 public void onPause() {
                                     super.onPause();
                                     Log.e("MediaPlayerService", "onPause");
                                     buildNotification(generateAction(android.R.drawable.
                                             ic_media_play, "Play", ACTION_PLAY));
                                     if (mMediaPlayer.isPlaying())
                                         mMediaPlayer.pause();
                                 }

                                 @Override
                                 public void onSkipToNext() {
                                     super.onSkipToNext();
                                     Log.e("MediaPlayerService", "onSkipToNext");
                                     //Change media here
                                     mCurrentIndex++;
                                     mCurrentIndex %= mTrackList.size();
                                     skipToIndex();
                                 }

                                 @Override
                                 public void onSkipToPrevious() {
                                     super.onSkipToPrevious();
                                     Log.e("MediaPlayerService", "onSkipToPrevious");
                                     mCurrentIndex = mCurrentIndex > 0 ? mCurrentIndex - 1
                                             : mTrackList.size() - 1;
                                     skipToIndex();
                                 }

                                 @Override
                                 public void onFastForward() {
                                     super.onFastForward();
                                     Log.e("MediaPlayerService", "onFastForward");
                                     //Manipulate current media here
                                     int currentPosition = mMediaPlayer.getCurrentPosition();
                                     if (currentPosition + mSeekValue <= mMediaPlayer.getDuration()) {
                                         mMediaPlayer.seekTo(currentPosition + mSeekValue);
                                     } else {
                                         mMediaPlayer.seekTo(mMediaPlayer.getDuration());
                                     }
                                 }

                                 @Override
                                 public void onRewind() {
                                     super.onRewind();
                                     Log.e("MediaPlayerService", "onRewind");
                                     int currentPosition = mMediaPlayer.getCurrentPosition();
                                     if (currentPosition - mSeekValue >= 0) {
                                         mMediaPlayer.seekTo(currentPosition - mSeekValue);
                                     } else {
                                         mMediaPlayer.seekTo(0);
                                     }
                                 }

                                 @Override
                                 public void onStop() {
                                     super.onStop();
                                     Log.e("MediaPlayerService", "onStop");
                                     //Stop media player here
                                     NotificationManager notificationManager = (NotificationManager)
                                             getApplicationContext().getSystemService(
                                                     Context.NOTIFICATION_SERVICE);
                                     Objects.requireNonNull(notificationManager).cancel(
                                             MUSIC_PLAYER_NOTIFICATION_ID);
                                     Intent intent = new Intent(getApplicationContext(),
                                             MediaPlayerService.class);
                                     stopService(intent);
                                     mMediaPlayer.release();
                                 }

                                 @Override
                                 public void onSeekTo(long pos) {
                                     super.onSeekTo(pos);
                                 }

                                 @Override
                                 public void onSetRating(@NonNull Rating rating) {
                                     super.onSetRating(rating);
                                 }
                             }
        );
    }


    private void skipToIndex() {
        currentMediaTitle = mTrackList.get(mCurrentIndex).getTitle();
        currentMediaArtist = mTrackList.get(mCurrentIndex).getArtist();
        buildNotification(generateAction(android.R.drawable.
                ic_media_pause, "Pause", ACTION_PAUSE));

        playSongNumber(mCurrentIndex);
    }

    private void playSongNumber(int index) {
        try {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            String filename = "android.resource://" + this.getPackageName() + "/raw/" +
                    mTrackList.get(index).getTitle();

            mMediaPlayer.setDataSource(this, Uri.parse(filename));
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(MediaPlayer::start);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mSession.release();
        return super.onUnbind(intent);
    }
}
