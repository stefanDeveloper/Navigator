package de.dhbw.navigator.controls;

import de.dhbw.navigator.views.RootLayoutController;
import javafx.animation.Animation.Status;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

/**
 * Rotate an ImageView
 * 
 * @author Stefan Machmeier, Manuela Leopold, Konrad Müller, Markus Menrath
 *
 */
public class RotationImageViewControl extends Thread {
	private RotateTransition rotateView;

	/**
	 * Set an Animation on an ImageView to turn around until it will be stopped
	 * by an event
	 * 
	 * @param imageButton
	 */
	public RotationImageViewControl(ImageButtonControl imageButton) {
		// Initialize
		rotateView = new RotateTransition(Duration.seconds(3), imageButton);
		rotateView.setToAngle(0);
		rotateView.setFromAngle(360);
		// Set Interpolator to linear
		rotateView.setInterpolator(Interpolator.LINEAR);
		// Set CyleCount endless, can only be stopped by stop() event
		rotateView.setCycleCount(RotateTransition.INDEFINITE);
		rotateView.statusProperty().addListener(new ChangeListener<Status>() {
			@Override
			public void changed(ObservableValue<? extends Status> observable, Status oldValue, Status newValue) {
				if (newValue == Status.STOPPED) {
					// Add new RotateTransition
					RotateTransition transition = new RotateTransition(Duration.seconds(1), imageButton);
					transition.setFromAngle(imageButton.getRotate());
					transition.setToAngle(0);
					transition.setCycleCount(1);
					transition.setAutoReverse(true);
					transition.play();
				}
			}
		});

	}

	/**
	 * Stop the rotation.
	 */
	public void stopRotation() {
		rotateView.stop();
	}

	/**
	 * Start the rotation.
	 */
	public void startRotation() {
		rotateView.play();
	}

	public void run() {
		synchronized (RootLayoutController.lock) {
			try {
				startRotation();
				System.out.println("Rotaion Thread wait");
				RootLayoutController.lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Rotation Thread notify");
			stopRotation();
			System.out.println("Rotation stopped");
		}

	}
}
