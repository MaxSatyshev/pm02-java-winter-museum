package museum.console;

public final class VisitorConsole {
	public static void main(String[] args) {
		int visitorAge = 19;
		System.out.println("Добро пожаловать в Музей музыки!");
		for (String word : new String[]{"Экспозиция", "Концерт", "Экскурсия"}) {
			System.out.println(word);
		}
		System.out.println("Возраст посетителя: " + visitorAge + " лет.");
	}
}
