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
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.blockly.android.webview.utility.Track;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaPlayerService extends Service {
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

    public MediaPlayerService() {

    }

    public void createNotification(final Context context, Track track) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.MediaStyle style = new Notification.MediaStyle();
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat
                    .from(context);
            MediaSession mediaSession = new MediaSession(context, "simple player session");

            //begin
            mediaSession.setCallback(new MediaSession.Callback() {
                                         @Override
                                         public void onPlay() {
                                             super.onPlay();
                                             Log.e("MediaPlayerService", "onPlay");
                                             buildNotification(generateAction(android.R.drawable.
                                                     ic_media_pause, "Pause", ACTION_PAUSE));
                                         }

                                         @Override
                                         public void onPause() {
                                             super.onPause();
                                             Log.e("MediaPlayerService", "onPause");
                                             buildNotification(generateAction(android.R.drawable.
                                                     ic_media_play, "Play", ACTION_PLAY));
                                         }

                                         @Override
                                         public void onSkipToNext() {
                                             super.onSkipToNext();
                                             Log.e("MediaPlayerService", "onSkipToNext");
                                             //Change media here
                                             buildNotification(generateAction(android.R.drawable.
                                                     ic_media_pause, "Pause", ACTION_PAUSE));
                                         }

                                         @Override
                                         public void onSkipToPrevious() {
                                             super.onSkipToPrevious();
                                             Log.e("MediaPlayerService", "onSkipToPrevious");
                                             //Change media here
                                             buildNotification(generateAction(android.R.drawable.
                                                     ic_media_pause, "Pause", ACTION_PAUSE));
                                         }

                                         @Override
                                         public void onFastForward() {
                                             super.onFastForward();
                                             Log.e("MediaPlayerService", "onFastForward");
                                             //Manipulate current media here
                                         }

                                         @Override
                                         public void onRewind() {
                                             super.onRewind();
                                             Log.e("MediaPlayerService", "onRewind");
                                             //Manipulate current media here
                                         }

                                         @Override
                                         public void onStop() {
                                             super.onStop();
                                             Log.e("MediaPlayerService", "onStop");
                                             //Stop media player here
                                             NotificationManager notificationManager = (NotificationManager)
                                                     context.getSystemService(
                                                             Context.NOTIFICATION_SERVICE);
                                             notificationManager.cancel(1);
                                             Intent intent = new Intent(context,
                                                     MediaPlayerService.class);
                                             stopService(intent);
                                         }

                                         @Override
                                         public void onSeekTo(long pos) {
                                             super.onSeekTo(pos);
                                         }

                                         @Override
                                         public void onSetRating(Rating rating) {
                                             super.onSetRating(rating);
                                         }
                                     }
            );
            //end

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), track.getImage());

            //start
            Intent intent = new Intent(context, MediaPlayerService.class);
            intent.setAction(ACTION_STOP);
            PendingIntent pendingIntent = PendingIntent.getService(context, 1, intent,
                    0);
            Notification.Builder builder = new Notification.Builder(context, CHANNEL_ID)
                    .setLargeIcon(icon) // album artwork icon
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setContentTitle("Media Title")
                    .setContentText("Media Artist")
                    .setDeleteIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .setShowWhen(false)
                    .setStyle(new Notification.MediaStyle().setMediaSession(
                            mediaSession.getSessionToken()));
//                    .setStyle(style);

            builder.addAction(generateAction(android.R.drawable.ic_media_previous,
                    "Previous", ACTION_PREVIOUS));
            builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind",
                    ACTION_REWIND));
//            builder.addAction(action);
            builder.addAction(generateAction(android.R.drawable.ic_media_ff,
                    "Fast Foward", ACTION_FAST_FORWARD));
            builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next",
                    ACTION_NEXT));
            style.setShowActionsInCompactView(0, 1, 2, 3, 4);
            //end

            notificationManagerCompat.notify(1, builder.build());
        }
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

    private Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildNotification(Notification.Action action) {
        Notification.MediaStyle style = new Notification.MediaStyle();
        mManager = NotificationManagerCompat
                .from(getApplicationContext());
        MediaSession mediaSession = new MediaSession(getApplicationContext(), "simple player session");
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.ic_launcher_background);
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),
                1, intent, 0);
        Notification.Builder builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setLargeIcon(icon) // album artwork icon
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("Media Title")
                .setContentText("Media Artist")
                .setDeleteIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setShowWhen(false)
                .setStyle(new Notification.MediaStyle().setMediaSession(
                        mediaSession.getSessionToken()));

        builder.addAction(generateAction(android.R.drawable.ic_media_previous,
                "Previous", ACTION_PREVIOUS));
        builder.addAction(generateAction(android.R.drawable.ic_media_rew, "Rewind", ACTION_REWIND));
        builder.addAction(action);
        builder.addAction(generateAction(android.R.drawable.ic_media_ff,
                "Fast Foward", ACTION_FAST_FORWARD));
        builder.addAction(generateAction(android.R.drawable.ic_media_next, "Next", ACTION_NEXT));
        style.setShowActionsInCompactView(0, 1, 2, 3, 4);

        mManager.notify(1, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mManager == null) {
            mTrackList = new ArrayList<>();
            mTrackList.add(new Track("a"));
            mTrackList.add(new Track("b"));
            mTrackList.add(new Track("c"));
            mTrackList.add(new Track("d"));
            mCurrentIndex = new SecureRandom().nextInt(mTrackList.size());
            initMediaSessions();
        }

        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initMediaSessions() {
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), getResources().getIdentifier(
                mTrackList.get(mCurrentIndex).getTitle(), "raw", getPackageName()));

        mSession = new MediaSession(getApplicationContext(), "simple player session");
        mController = new MediaController(getApplicationContext(), mSession.getSessionToken());

        mSession.setCallback(new MediaSession.Callback() {
                                 @RequiresApi(api = Build.VERSION_CODES.O)
                                 @Override
                                 public void onPlay() {
                                     super.onPlay();
                                     Log.e("MediaPlayerService", "onPlay");
                                     buildNotification(generateAction(android.R.drawable.
                                             ic_media_pause, "Pause", ACTION_PAUSE));
                                     if (!mMediaPlayer.isPlaying())
                                         mMediaPlayer.start();
                                 }

                                 @RequiresApi(api = Build.VERSION_CODES.O)
                                 @Override
                                 public void onPause() {
                                     super.onPause();
                                     Log.e("MediaPlayerService", "onPause");
                                     buildNotification(generateAction(android.R.drawable.
                                             ic_media_play, "Play", ACTION_PLAY));
                                     if (mMediaPlayer.isPlaying())
                                         mMediaPlayer.pause();
                                 }

                                 @RequiresApi(api = Build.VERSION_CODES.O)
                                 @Override
                                 public void onSkipToNext() {
                                     super.onSkipToNext();
                                     Log.e("MediaPlayerService", "onSkipToNext");
                                     //Change media here
                                     buildNotification(generateAction(android.R.drawable.
                                             ic_media_pause, "Pause", ACTION_PAUSE));

                                     mCurrentIndex++;
                                     mCurrentIndex %= mTrackList.size();
                                     playSongNumber(mCurrentIndex);
                                 }

                                 @RequiresApi(api = Build.VERSION_CODES.O)
                                 @Override
                                 public void onSkipToPrevious() {
                                     super.onSkipToPrevious();
                                     Log.e("MediaPlayerService", "onSkipToPrevious");
                                     //Change media here
                                     buildNotification(generateAction(android.R.drawable.
                                             ic_media_pause, "Pause", ACTION_PAUSE));

                                     mCurrentIndex = mCurrentIndex > 0 ? mCurrentIndex - 1
                                             : mTrackList.size() - 1;
                                     playSongNumber(mCurrentIndex);
                                 }

                                 @Override
                                 public void onFastForward() {
                                     super.onFastForward();
                                     Log.e("MediaPlayerService", "onFastForward");
                                     //Manipulate current media here
                                     mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition()
                                             + mSeekValue);
                                 }

                                 @Override
                                 public void onRewind() {
                                     super.onRewind();
                                     Log.e("MediaPlayerService", "onRewind");
                                     //Manipulate current media here
                                     mMediaPlayer.seekTo(mMediaPlayer.getCurrentPosition()
                                             - mSeekValue);
                                 }

                                 @Override
                                 public void onStop() {
                                     super.onStop();
                                     Log.e("MediaPlayerService", "onStop");
                                     //Stop media player here
                                     NotificationManager notificationManager = (NotificationManager)
                                             getApplicationContext().getSystemService(
                                                     Context.NOTIFICATION_SERVICE);
                                     Objects.requireNonNull(notificationManager).cancel(1);
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

    private void playSongNumber(int index) {
        try {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            String filename = "android.resource://" + this.getPackageName() + "/raw/" +
                    mTrackList.get(index).getTitle();
            mMediaPlayer.setDataSource(this, Uri.parse(filename));
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

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
