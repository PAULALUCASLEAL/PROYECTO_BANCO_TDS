module ASP.BanCroak {
    requires transitive javafx.controls;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
	requires javafx.media;
	requires javafx.base;
	requires transitive javafx.graphics;
    exports ASP.BanCroak.domain;
    exports ASP.BanCroak.repo;
    exports ASP.BanCroak.filtros;
    exports ASP.BanCroak.persistence;
    exports ASP.BanCroak.ui.app;
    exports ASP.BanCroak.ui.main;
    exports ASP.BanCroak.ui.gastos;
    exports ASP.BanCroak.ui.cuentas;
    exports ASP.BanCroak.ui.graficas;
    exports ASP.BanCroak.ui.visualizar;
    exports ASP.BanCroak.ui.notificaciones;
    exports ASP.BanCroak.service;
    exports ASP.BanCroak.application;
    exports ASP.BanCroak.cli;

    opens ASP.BanCroak.persistence to com.fasterxml.jackson.databind;
}
