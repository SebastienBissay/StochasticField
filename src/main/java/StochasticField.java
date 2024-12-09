import processing.core.PApplet;
import processing.core.PVector;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class StochasticField extends PApplet {
    public static void main(String[] args) {
        PApplet.main(StochasticField.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
        stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        blendMode(BLEND_MODE);
        noFill();
        noLoop();
    }

    @Override
    public void draw() {
        for (float x = 0; x < WIDTH; x += STEP) {
            for (float y = 0; y < HEIGHT; y += STEP) {
                PVector p = new PVector(x + INITIAL_VARIANCE * STEP * randomGaussian(),
                        y + INITIAL_VARIANCE * STEP * randomGaussian());
                for (int l = 0; l < MAX_LENGTH; l++) {
                    if (p.x >= MARGIN && p.x <= WIDTH - MARGIN && p.y >= MARGIN && p.y <= HEIGHT - MARGIN) {
                        point(p.x, p.y);
                    }

                    float noise = myNoise(x, y);
                    float diff = 0;
                    float angle = 0;
                    for (int k = 0; k < NUMBER_OF_ANGLES; k++) {
                        float tmpAngle = random(TWO_PI);
                        float tmpNoise = myNoise(p.x + NOISE_VECTOR_LENGTH * cos(tmpAngle),
                                p.y + NOISE_VECTOR_LENGTH * sin(tmpAngle));
                        if (abs(tmpNoise - noise) > diff) {
                            diff = abs(tmpNoise - noise);
                            angle = tmpAngle;
                        }
                    }
                    p.add(PVector.fromAngle(angle).mult(NOISE_VECTOR_LENGTH));
                }
            }
        }
        saveSketch(this);
    }

    private float myNoise(float x, float y) {
        float scale = map(dist(x, y, WIDTH / 2f, HEIGHT / 2f),
                0, min(WIDTH, HEIGHT) / sqrt(2),
                NOISE_LOW_SCALE, NOISE_HIGH_SCALE);
        return noise(x * scale, y * scale);
    }
}
