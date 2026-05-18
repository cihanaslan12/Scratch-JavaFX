package scratch.viewmodel;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import scratch.model.*;

public class WorldViewModel {
    private final Monde monde;
    private final Programme programModel;
    private final ObservableList<Action> program;
    private final BooleanProperty loaded = new SimpleBooleanProperty(false); // cliquer sur Charger
    private final BooleanProperty stepping = new SimpleBooleanProperty(false); // bouton executer en mode suivant
    private final StringProperty loadButtonText = new SimpleStringProperty("Charger");
    private final StringProperty runButtonText = new SimpleStringProperty("Executer");
    private final BooleanProperty canLoad = new SimpleBooleanProperty(false);
    // index de l'action en cours d'execution dans le program du monde
    private final IntegerProperty execIdx = new SimpleIntegerProperty(0);
    private final IntegerProperty highlightIdx = new SimpleIntegerProperty(-1);
    private final BooleanProperty hasError = new SimpleBooleanProperty(false);
    private final Timeline executeAuto = new Timeline();
    private final DoubleProperty speed = new SimpleDoubleProperty(1.0);
    private final BooleanProperty isRunning = new SimpleBooleanProperty(false);

    public WorldViewModel(Programme program) {
        this.monde = new Monde();
        this.programModel = program;
        this.program = programModel.getProgram();

        // initialise l'exécution automatique
        keyFrame();

        //mise à jour de la vitesse(du timeline) lorsque la vitesse du slider change
        speed.addListener((obs, oldVal, newVal) -> {
            executeAuto.setRate(newVal.doubleValue());
        });
    }

    public BooleanProperty loadedProperty() {
        return loaded;
    }

    public IntegerProperty execIdxProperty() {
        return execIdx;
    }

    public IntegerProperty highlightIdxProperty() {
        return highlightIdx;
    }

    public BooleanProperty hasErrorProperty() {
        return hasError;
    }

    public BooleanProperty isRunningProperty() {
        return isRunning;
    }

    public ObservableList<VarDeclaration> getVariables() {
        return monde.getVariables();
    }

    public BooleanProperty canLoadProperty() {
        return canLoad;
    }

    public BooleanBinding canRun () {
        return Bindings.createBooleanBinding(() -> {
            int size = program.size();
            return loaded.get() && size > 0 && execIdx.get() < size && !hasError.get();
        }, loaded, execIdx, program, hasError);
    }

    public void loadOrReset () {
        if (!program.isEmpty()) {
            monde.reset();
            execIdx.set(0);
            highlightIdx.set(-1);
            stepping.set(false);
            hasError.set(false);
            runButtonText.set("Executer");

            if ((loaded.get())) {
                loaded.set(false);
                loadButtonText.set("Charger");
            } else {
                loaded.set(true);
                loadButtonText.set("Ré-initialiser");
            }
        }
    }

    public void execOrNext() {
        int size = program.size();
        if (loaded.get() && size > 0) {
            // premier clic sur Executer -> sélectionne la première ligne du prog
            if (!stepping.get()) {
                stepping.set(true);
                if (!isRunning.get()) {     // si exec auto -> pas de btn suivant
                    runButtonText.set("Suivant");
                }
                highlightIdx.set(0);
            } else {
                // mode Suivant
                // try catch -> quand il y a une erreur a l'éxecution, le bouton Suivant est désactivé
                try {
                    execIdx.set(programModel.executeNext(execIdx.get(), monde));
                    if (execIdx.get() < size) {
                        highlightIdx.set(execIdx.get());
                    } else {
                        stepping.set(false);
                        runButtonText.set("Executer");
                        highlightIdx.set(size - 1);
                    }
                } catch (RuntimeException e) {
                    hasError.set(true);
                    stepping.set(false);
                    runButtonText.set("Executer");

                    e.printStackTrace();

                    if (execIdx.get() >= 0 && execIdx.get() < size) {
                        highlightIdx.set(execIdx.get());
                    }
                }
            }
        }
    }

    // méthode qui lie le temps d'execution et les méthodes d'execution à l'execution auto
    private void keyFrame() {
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), e -> {
            if (canRun().get()) {
                execOrNext();
            } else {
                stopExec();
            }
        });
        executeAuto.getKeyFrames().add(keyFrame);
        executeAuto.setCycleCount(Animation.INDEFINITE);
    }

    public DoubleProperty speedProperty() {
        return speed;
    }

    public void startAutoExec() {
        executeAuto.play();
        isRunning.set(true);
    }

    public void stopExec() {
        executeAuto.stop();
        isRunning.set(false);
    }

    // retourne un texte décrivant la position et l'angle de la tortue
    public StringBinding turtlePosition() {
        return Bindings.createStringBinding(() -> {
                    double x = monde.getPosPersonnageX().get() - getWorldOriginX();
                    double y = getWorldOriginY() - monde.getPosPersonnageY().get();
                    double a = monde.getPersonnageAngle().get();
                    double angle = (a % 360 + 360) % 360;   // calcul de l'angle de 0 à 359
                    return String.format("Tortue: x = %.1f, y = %.1f, direction = %.1f °", x, y, angle);
                }, monde.getPosPersonnageX(), monde.getPosPersonnageY(), monde.getPersonnageAngle()
        );
    }

    public Monde getMonde () {
        return monde;
    }
    public int getWorldSize() {
        return monde.getWorldSize();
    }
    public int getWorldOriginX() {
        return monde.getWorldOriginX();
    }
    public int getWorldOriginY() {
        return monde.getWorldOriginY();
    }
    public DoubleProperty getPosX(){
        return monde.getPosPersonnageX();
    }
    public DoubleProperty getPosY() {
        return monde.getPosPersonnageY();
    }
    public DoubleProperty getAngleProperty() {
        return monde.getPersonnageAngle();
    }
    public double getAngle() {
        return monde.getPersonnageAngle().get();
    }
    public ObservableList<Segment> getSegments() {
        return monde.getSegments();
    }

    public StringProperty loadButtonTextProperty () {
        return loadButtonText;
    }
    public StringProperty runButtonTextProperty () {
        return runButtonText;
    }
    public void invalidateWorld() {
        execIdx.set(0);
        loaded.set(false);
        stepping.set(false);
        hasError.set(false);
        loadButtonText.set("Charger");
        runButtonText.set("Executer");
    }
}
