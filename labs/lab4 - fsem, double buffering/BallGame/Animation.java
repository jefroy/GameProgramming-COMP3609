import java.awt.Image;
import java.util.ArrayList;
import java.awt.Graphics2D;
import javax.swing.*;

/**
    The Animation class manages a series of images (frames) and
    the amount of time to display each frame.
*/
public class Animation {

    private JFrame window;					// JPanel on which animation is being displayed
    private ArrayList frames;					// collection of images for animation frames
    private int currFrameIndex;					// current frame being displayed
    private long animTime;					// time that the animation has run for already
    private long startTime;					// start time of the animation
    private long totalDuration;					// total duration of the animation


    /**
        Creates a new, empty Animation.
    */
    public Animation(JFrame f) {
	window = f;
        frames = new ArrayList();
        totalDuration = 0;
        start();
    }


    /**
        Adds an image to the animation with the specified
        duration (time to display the image).
    */
    public synchronized void addFrame(Image image, long duration)
    {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }


    /**
        Starts this animation over from the beginning.
    */
    public synchronized void start() {
        animTime = 0;						// reset time animation has run for to zero
        currFrameIndex = 0;					// reset current frame to first frame
	startTime = System.currentTimeMillis();			// reset start time to current time
    }


    /**
        Updates this animation's current image (frame), if
        neccesary.
    */
    public synchronized void update() {
        long currTime = System.currentTimeMillis();		// find the current time
	long elapsedTime = currTime - startTime;		// find how much time has elapsed since last update
	startTime = currTime;					// set start time to current time

        if (frames.size() > 1) {
            animTime += elapsedTime;				// add elapsed time to amount of time animation has run for
            if (animTime >= totalDuration) {			// if the time animation has run for > total duration
                animTime = animTime % totalDuration;		//    reset time animation has run for
                currFrameIndex = 0;				//    reset current frame to first frame
            }

            while (animTime > getFrame(currFrameIndex).endTime) {
                currFrameIndex++;				// set frame corresponding to time animation has run for
            }
        }
    }


    /**
        Gets this Animation's current image. Returns null if this
        animation has no images.
    */
    public synchronized Image getImage() {
        if (frames.size() == 0) {
            return null;
        }
        else {
            return getFrame(currFrameIndex).image;
        }
    }

    public void draw (Graphics2D g2) {				// draw the current frame on the JPanel
        g2.drawImage(getImage(), 0, 0, null);
    }

    public int getNumFrames() {					// find out how many frames in animation
	return frames.size();
    }

    private AnimFrame getFrame(int i) {
        return (AnimFrame)frames.get(i);
    }


    private class AnimFrame {

        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}
