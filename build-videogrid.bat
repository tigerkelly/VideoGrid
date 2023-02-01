echo on
@if [%1] == [] (echo No argument given. && exit /B 2) 
@copy ws-2020-9\VideoGrid\videogrid.jar input_videogrid\videogrid.jar
@copy ws-2020-9\VideoGrid\images\VideoGrid.png input_videogrid\VideoGrid.png
@copy ws-2020-9\VideoGrid\inifile8-2.0.16.jar input_videogrid\inifile8-2.0.16.jar
@del "output\VideoGrid-%1.exe"
jpackage --type exe --name "VideoGrid" --description "Video Grid Program" --vendor "Kellyw" --app-version %1 --input input_VideoGrid --dest output --main-jar videogrid.jar --main-class application.Main --module-path "C:\javafx-jmods-19;%PATH_TO_FX%" --add-modules javafx.base,javafx.graphics,javafx.controls,javafx.fxml,java.desktop --file-associations run.properties --icon ws-2020-9\VideoGrid\images\VideoGrid.ico --jlink-options --bind-services --win-shortcut --win-menu
