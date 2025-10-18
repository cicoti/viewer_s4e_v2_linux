; Script atualizado para remover JAVA_HOME

#define MyAppName "s4etech-viewer"
#define MyAppVersion "1.1.02"
#define MyAppPublisher "s4etech"
#define MyAppURL "https://www.s4e.tech/br/"

[Setup]
AppId={{29361B5B-35B4-4C4A-BF1F-F7BB670A17C0}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName=C:\{#MyAppName}
DefaultGroupName={#MyAppName}
OutputDir=C:\Users\silvi\OneDrive\Desktop
OutputBaseFilename=s4etech-viewer-instalador-1102
SetupIconFile=C:\projetos\ctech\executavel\s4e-viewer\icos4e.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "brazilianportuguese"; MessagesFile: "compiler:Languages\BrazilianPortuguese.isl"

[Files]
Source: "C:\projetos\ctech\executavel\s4e-viewer\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Registry]
Root: HKLM; Subkey: "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; \
    ValueType: expandsz; ValueName: "Path"; \
    ValueData: "{olddata};C:\Program Files\Java\jdk-22\bin"; \
    Check: not IsPathInSystemPath('C:\Program Files\Java\jdk-22\bin')

[Icons]
Name: "{commondesktop}\s4etech-viewer"; Filename: "{app}\viewer.exe"; IconFilename: "{app}\icos4e.ico"; WorkingDir: "{app}"

[Code]
function IsPathInSystemPath(const S: string): Boolean;
var
  Path: string;
begin
  if RegQueryStringValue(HKLM, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment', 'Path', Path) then
  begin
    Result := Pos(';' + Uppercase(S) + ';', ';' + Uppercase(Path) + ';') > 0;
  end
  else
    Result := False;
end;

procedure RemoveJavaHome();
var
  ErrorCode: Integer;
begin
  if not ShellExec('open', 'powershell.exe', '-NoProfile -ExecutionPolicy Bypass -Command "' +
    '[Environment]::SetEnvironmentVariable(''JAVA_HOME'', $null, [EnvironmentVariableTarget]::Machine)"', '', SW_HIDE, ewWaitUntilTerminated, ErrorCode) then
  begin
    MsgBox('Falha ao remover JAVA_HOME. C�digo de erro: ' + SysErrorMessage(ErrorCode), mbError, MB_OK);
  end;
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    RemoveJavaHome();
    MsgBox('Instala��o conclu�da com sucesso! O Java vers�o 22 foi configurado corretamente no Path e a vari�vel JAVA_HOME foi removida.', mbInformation, MB_OK);
  end;
end;
