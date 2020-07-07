module app.module {
    //requires kotlin.stdlib;
    requires javafx.controls;
    requires javafx.media;
    requires javafx.fxml;
    requires tornadofx;
    requires org.testfx;
    requires com.squareup.moshi;
    requires okio;
    requires org.slf4j;
    requires io.reactivex.rxjava2;

    exports com.paulnogas.loganalyzer.view;
    opens com.paulnogas.loganalyzer.app to tornadofx, javafx.graphics;
    opens com.paulnogas.loganalyzer.controller to tornadofx;
    opens com.paulnogas.loganalyzer.view to org.testfx.junit5;
}