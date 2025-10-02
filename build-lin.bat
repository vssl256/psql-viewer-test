@echo on
rd /s /q jre PSQLView-lin
jlink --module-path "jdk11\jmods" --add-modules java.base,javafx.controls,javafx.fxml --output jre --strip-debug --compress=2 --no-header-files --no-man-pages && java -jar packr-all-4.0.0.jar --platform linux64 --jdk jre --executable PSQLView --classpath dbtest.jar --mainclass vessel.Main --output PSQLView-lin