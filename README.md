## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).


## Run Example
c: && cd c:\ITwork\java\encoder && cmd /C "C:\jdk-17\bin\java.exe @C:\Users\swkao\AppData\Local\Temp\cp_3erqd8t84gkpe9aby113ur391.argfile Sha256Encoder -e25664 .\src\userlist.txt"

## 실행시 classpath 환경변수 세팅
set CLASSPATH=.\lib\commons-cli-1.5.0.jar;.\bin
echo %CLASSPATH%
java Sha256Encoder -e256 .\src\userlist.txt
