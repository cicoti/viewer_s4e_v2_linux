; Script atualizado para remover JAVA_HOME e manter dependências adicionais corretamente

#define MyAppName "S4ETech-Viewer"
#define MyAppVersion "2.1.1"
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
OutputBaseFilename=s4etech-viewer-instalador-211
SetupIconFile=C:\projetos\ctech\executavel\s4e-viewer\icos4e.ico
Compression=lzma
SolidCompression=yes
WizardStyle=modern

[Languages]
Name: "brazilianportuguese"; MessagesFile: "compiler:Languages\BrazilianPortuguese.isl"

[Files]
; Copia tudo EXCETO as pastas licenca e configuracao
Source: "C:\projetos\ctech\executavel\s4e-viewer\*"; DestDir: "{app}"; Excludes: "licenca\*,configuracao\*"; Flags: ignoreversion recursesubdirs createallsubdirs

; Pastas preservadas após desinstalação
Source: "C:\projetos\ctech\executavel\s4e-viewer\licenca\*"; DestDir: "{app}\licenca"; Flags: ignoreversion recursesubdirs createallsubdirs uninsneveruninstall
Source: "C:\projetos\ctech\executavel\s4e-viewer\configuracao\*"; DestDir: "{app}\configuracao"; Flags: ignoreversion recursesubdirs createallsubdirs uninsneveruninstall

; Ícone apenas se não existir
Source: "C:\projetos\ctech\executavel\s4e-viewer\icos4e.ico"; DestDir: "{app}"; Flags: onlyifdoesntexist


[Registry]
Root: HKLM; Subkey: "SYSTEM\CurrentControlSet\Control\Session Manager\Environment"; \
    ValueType: expandsz; ValueName: "Path"; \
    ValueData: "{olddata}"

[Icons]
Name: "{commondesktop}\s4etech-viewer"; Filename: "{app}\viewer.exe"; IconFilename: "{app}\icos4e.ico"; WorkingDir: "{app}"

[Code]
procedure AddToPath(const NewPath: string);
var
  Path: string;
begin
  if RegQueryStringValue(HKLM, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment', 'Path', Path) then
  begin
    if Pos(';' + LowerCase(NewPath) + ';', ';' + LowerCase(Path) + ';') = 0 then
    begin
      RegWriteStringValue(HKLM, 'SYSTEM\CurrentControlSet\Control\Session Manager\Environment', 'Path', Path + ';' + NewPath);
    end;
  end;
end;

procedure RemoveJavaHome();
var
  ErrorCode: Integer;
begin
  if not ShellExec('open', 'powershell.exe', '-NoProfile -ExecutionPolicy Bypass -Command "' +
    '[Environment]::SetEnvironmentVariable(''JAVA_HOME'', $null, [EnvironmentVariableTarget]::Machine)"', '', SW_HIDE, ewWaitUntilTerminated, ErrorCode) then
  begin
    MsgBox('Falha ao remover JAVA_HOME. Código de erro: ' + SysErrorMessage(ErrorCode), mbError, MB_OK);
  end;
end;

procedure RefreshEnvironment();
var
  ErrorCode: Integer;
begin
  if not ShellExec('open', 'powershell.exe', '-NoProfile -ExecutionPolicy Bypass -Command "' +
    '[System.Environment]::SetEnvironmentVariable(''Path'', [System.Environment]::GetEnvironmentVariable(''Path'', ''Machine''), ''Process'')"', '', SW_HIDE, ewWaitUntilTerminated, ErrorCode) then
  begin
    MsgBox('Falha ao atualizar o ambiente. Código de erro: ' + SysErrorMessage(ErrorCode), mbError, MB_OK);
  end;
end;

procedure CurStepChanged(CurStep: TSetupStep);
begin
  if CurStep = ssPostInstall then
  begin
    AddToPath('C:\Program Files\Java\jdk-22\bin');
    AddToPath('C:\s4etech-viewer\gstreamer\1.0\msvc_x86_64\bin');
    AddToPath('C:\s4etech-viewer\pcan\64-bit');
    RemoveJavaHome();
    RefreshEnvironment();
    MsgBox('Instalação concluída com sucesso!', mbInformation, MB_OK);
  end;
end;