module ASP.BanCroak {
    requires javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    exports ASP.BanCroak.domain;
    exports ASP.BanCroak.repo;
    exports ASP.BanCroak.filtros;
    exports ASP.BanCroak.persistence;
    exports ASP.BanCroak.ui.app;
    exports ASP.BanCroak.ui.main;
    exports ASP.BanCroak.ui.gastos;
    exports ASP.BanCroak.ui.cuentas;
    exports ASP.BanCroak.ui.graficas;

    opens ASP.BanCroak.persistence to com.fasterxml.jackson.databind;
}
