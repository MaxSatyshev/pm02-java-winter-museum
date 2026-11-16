# Производственная практика ПМ.02 · Java

Предметная область — информационная система музея. Основной результат — локальный учебный прототип раздела афиши Российского национального музея музыки. Иллюстрация здания, знак с лирой и снежинка созданы в коде. Карточки содержат темы для знакомства с коллекцией.

## Состав

| Проект | Результат |
| --- | --- |
| MuseumConsole | Java-проект Eclipse. Вывод приветствия, слов столбцом и возраста одной строкой. |
| TicketCalculator | Maven-проект Apache NetBeans. Форма Swing, четыре операции и обработка ошибочного ввода. |
| WinterMuseum | Зимняя афиша Swing, анимация графических частиц, пауза и продолжение. |
| checks | 12 проверок вычислений и модели анимации. |

## Запуск готовых JAR

Установите JDK 21 для Windows x64 или распакуйте его ZIP. В PowerShell из каталога проекта укажите свой путь к JDK. Команды ниже меняют переменные только текущего сеанса.

~~~powershell
$env:JAVA_HOME = 'C:\путь\к\jdk-21'
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
java -version
javac -version
java -jar .\dist\WinterMuseum.jar
java -jar .\dist\TicketCalculator.jar
java -jar .\dist\MuseumConsole.jar
~~~

Для повторной сборки и проверки выполните .\build.ps1 -Verify. Кнопка «Пауза» в афише останавливает снежинки. «Продолжить» возобновляет движение.

## Упражнения в IDE

1. Eclipse IDE for Java Developers. Откройте File → Import → General → Existing Projects into Workspace. Укажите учебный MuseumConsole.zip из комплекта. У импортированного проекта проверьте JavaSE-21 и кодировку UTF-8. Запустите музейный класс через Run As → Java Application.
2. В проекте задан профиль с отступами Tabs only. Настройка Eclipse расположена в Java → Code Style → Formatter. Встроенный профиль можно скопировать в пользовательский и отредактировать.
3. В Eclipse использованы операции JDT над рабочей областью. Класс Greeting переименован в VisitorConsole, переменная age — в visitorAge. Создан пакет museum.console, класс перенесён в него, проект упакован в ZIP, снят с регистрации в рабочей области и восстановлен из ZIP. Команды интерфейса для повторения — Refactor → Rename, New → Package, Refactor → Move, Export → General → Archive File и Import → Existing Projects into Workspace.
4. В NetBeans откройте папку TicketCalculator через File → Open Project и запустите Run Project. Maven при первой сборке загрузит плагины. Можно сразу запустить класс museum.calc.CalcWindow или готовый JAR. Форма составлена в Java-коде Swing.

В использованной среде JDK и IDE развёрнуты из ZIP для Windows x64. Проверены Oracle JDK 21.0.12.1, Eclipse IDE 2025-06 R и Apache NetBeans 25. Если Windows продолжает запускать Java 8, проверьте порядок путей через where.exe java и используйте java.exe из выбранного JDK. Для NetBeans можно явно задать JDK параметром --jdkhome, для Eclipse — параметром -vm.

## GitHub

Создайте пустой репозиторий в своём аккаунте и скопируйте его HTTPS-адрес. Git-архив practice.bundle из комплекта содержит обе ревизии и ветку main. Публикации подлежит отдельный репозиторий с кодом.

~~~powershell
git clone practice.bundle pm02-java
cd pm02-java
git remote set-url origin https://github.com/ВАШ_ЛОГИН/ИМЯ_РЕПОЗИТОРИЯ.git
git push -u origin main
~~~

### Источники

- Музей музыки — https://music-museum.ru/museums/music.html
- JDK — https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html
- Eclipse — https://www.eclipse.org/downloads/packages/release/2025-06/r/eclipse-ide-java-developers
- Apache NetBeans — https://netbeans.apache.org/front/main/download/nb25/
- Swing Timer — https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/swing/Timer.html
- GitHub Docs — https://docs.github.com/en/repositories/creating-and-managing-repositories/creating-a-new-repository
