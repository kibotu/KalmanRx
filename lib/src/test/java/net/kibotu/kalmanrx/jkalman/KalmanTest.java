/*******************************************************************************
 * JKalman - KALMAN FILTER (Java) TestBench
 * <p>
 * Copyright (C) 2007 Petr Chmelar
 * <p>
 * By downloading, copying, installing or using the software you agree to
 * the license in licenseIntel.txt or in licenseGNU.txt
 * <p>
 * ***************************************************************************
 */

package net.kibotu.kalmanrx.jkalman;

import net.kibotu.kalmanrx.jama.Matrix;

import org.junit.Test;

import java.util.Random;


/**
 * JKalman TestBench
 */
public class KalmanTest {

    @Test
    public void random() {

        try {
            JKalman kalman = new JKalman(4, 2);

            Random rand = new Random(System.currentTimeMillis() % 2011);
            double x = 0;
            double y = 0;
            // constant velocity
            double dx = rand.nextDouble();
            double dy = rand.nextDouble();

            // init
            Matrix s = new Matrix(4, 1); // state [x, y, dx, dy, dxy]        
            Matrix c = new Matrix(4, 1); // corrected state [x, y, dx, dy, dxy]

            Matrix m = new Matrix(2, 1); // measurement [x]
            m.set(0, 0, x);
            m.set(1, 0, y);

            // transitions for x, y, dx, dy
            double[][] tr = {{1, 0, 1, 0},
                    {0, 1, 0, 1},
                    {0, 0, 1, 0},
                    {0, 0, 0, 1}};
            kalman.setTransition_matrix(new Matrix(tr));

            // 1s somewhere?
            kalman.setError_cov_post(kalman.getError_cov_post().identity());

            // init first assumption similar to first observation (cheat :)
            // kalman.setState_post(kalman.getState_post());

            // report what happend first :)
            System.out.println("first x:" + x + ", y:" + y + ", dx:" + dx + ", dy:" + dy);
            System.out.println("no; x; y; dx; dy; predictionX; predictionY; predictionDx; predictionDy; correctionX; correctionY; correctionDx; correctionDy;");

            // For debug only
            for (int i = 0; i < 200; ++i) {

                // check state before
                s = kalman.Predict();

                // function init :)
                // m.set(1, 0, rand.nextDouble());
                x = rand.nextGaussian();
                y = rand.nextGaussian();

                m.set(0, 0, m.get(0, 0) + dx + rand.nextGaussian());
                m.set(1, 0, m.get(1, 0) + dy + rand.nextGaussian());

                // a missing value (more then 1/4 times)
                if (rand.nextGaussian() < -0.8) {
                    System.out.println("" + i + ";;;;;"
                            + s.get(0, 0) + ";" + s.get(1, 0) + ";" + s.get(2, 0) + ";" + s.get(3, 0) + ";");
                } else { // measurement is ok :)
                    // look better
                    c = kalman.Correct(m);

                    System.out.println("" + i + ";" + m.get(0, 0) + ";" + m.get(1, 0) + ";" + x + ";" + y + ";"
                            + s.get(0, 0) + ";" + s.get(1, 0) + ";" + s.get(2, 0) + ";" + s.get(3, 0) + ";"
                            + c.get(0, 0) + ";" + c.get(1, 0) + ";" + c.get(2, 0) + ";" + c.get(3, 0) + ";");
                }

            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
