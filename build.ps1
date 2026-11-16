param(
    [string]$JdkHome = $env:JAVA_HOME,
    [switch]$Verify
)

$ErrorActionPreference = 'Stop'
$root = $PSScriptRoot
if ($JdkHome) {
    $javac = Join-Path $JdkHome 'bin/javac.exe'
    $java = Join-Path $JdkHome 'bin/java.exe'
    $jar = Join-Path $JdkHome 'bin/jar.exe'
} else {
    $javac = (Get-Command javac.exe -ErrorAction Stop).Source
    $java = Join-Path (Split-Path $javac) 'java.exe'
    $jar = Join-Path (Split-Path $javac) 'jar.exe'
}
$projects = @(
    @{ Name = 'MuseumConsole'; Main = 'museum.console.VisitorConsole'; Sources = 'MuseumConsole/src' },
    @{ Name = 'TicketCalculator'; Main = 'museum.calc.CalcWindow'; Sources = 'TicketCalculator/src/main/java' },
    @{ Name = 'WinterMuseum'; Main = 'museum.winter.WinterPoster'; Sources = 'WinterMuseum/src' }
)
$dest = (New-Item -ItemType Directory -Force (Join-Path $root 'dist')).FullName
$bins = foreach ($project in $projects) {
    $bin = (New-Item -ItemType Directory -Force (Join-Path $root "build/$($project.Name)")).FullName
    $sources = @(Get-ChildItem -LiteralPath (Join-Path $root $project.Sources) -Recurse -Filter '*.java' |
        ForEach-Object FullName)
    & $javac --release 21 -encoding UTF-8 -d $bin @sources
    if ($LASTEXITCODE -ne 0) { throw "javac failed for $($project.Name)" }
    & $jar --create --file (Join-Path $dest "$($project.Name).jar") --main-class $project.Main -C $bin .
    if ($LASTEXITCODE -ne 0) { throw "jar failed for $($project.Name)" }
    $bin
}
if ($Verify) {
    $tests = (New-Item -ItemType Directory -Force (Join-Path $root 'build/verify')).FullName
    $classpath = $bins -join [IO.Path]::PathSeparator
    & $javac --release 21 -encoding UTF-8 -cp $classpath -d $tests (Join-Path $root 'checks/Verify.java')
    if ($LASTEXITCODE -ne 0) { throw 'Verify compilation failed' }
    & $java '-Dstdout.encoding=UTF-8' -cp "$classpath$([IO.Path]::PathSeparator)$tests" Verify
    if ($LASTEXITCODE -ne 0) { throw 'A verification case failed' }
}
Write-Host "JAR: $dest"
