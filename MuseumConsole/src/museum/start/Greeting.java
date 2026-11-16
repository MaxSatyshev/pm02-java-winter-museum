package museum.start;

public final class Greeting {
	public static void main(String[] args) {
		int age = 19;
		System.out.println("Добро пожаловать в Музей музыки!");
		for (String word : new String[]{"Экспозиция", "Концерт", "Экскурсия"}) {
			System.out.println(word);
		}
		System.out.println("Возраст посетителя: " + age + " лет.");
	}
}
