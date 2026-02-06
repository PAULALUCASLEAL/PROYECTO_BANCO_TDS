module ASP.BanCroak {
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    exports ASP.BanCroak;
    exports ASP.BanCroak.ui;
    opens ASP.BanCroak.dto to com.fasterxml.jackson.databind;
}
