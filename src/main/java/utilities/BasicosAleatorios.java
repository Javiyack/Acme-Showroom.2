package utilities;

import java.util.Random;

public class BasicosAleatorios {
	static Random r = new Random();
	// sattic Random r = new Random(System.currentTimeMillis());

	public static Integer getNumeroAleatorio(int max) {

		return r.nextInt(max);
	}

	public static long getNumeroAleatorio(long max) {
		long res = 0;
		if ((max> 0)) {
			int repeticiones = (int) (max / 2147483647);
			int resto = (int) (max % 2147483647);
			
			while (repeticiones > 0) {
				res += r.nextInt(2147483647);
				repeticiones--;
			}
			res += r.nextInt(resto);
		} 
		return res;
	}

	public static Integer getNumeroAleatorio(int min, int max) {
		return r.nextInt(max - min + 1) + min;
	}

	public static Character getLetraMinusculaAleatoria() {
		String abecedario = "abcdefghijklmnñopqrstuvwxyz";

		return abecedario.charAt(getNumeroAleatorio(27));
	}

	public static Character getConsonanteMinusculaAleatoria() {
		String abecedario = "bcdfghjklmnñpqrstvwxyz";

		return abecedario.charAt(getNumeroAleatorio(27));
	}

	public static Character getVocalMinusculaAleatoria() {
		String abecedario = "aeiou";

		return abecedario.charAt(getNumeroAleatorio(5));
	}
	public static String getCodigoAlfabeticoAleatorioEnNinusculas(int longitud) {
		String res = "";
		while (res.length() < longitud) {
			res += BasicosAleatorios.getLetraMinusculaAleatoria();
		}
		return res.toLowerCase();

	}
	public static String getCodigoAlfabeticoAleatorioEnNinusculas() {
		String res = "";
		while (res.length() < getNumeroAleatorio(5,10)) {
			res += BasicosAleatorios.getLetraMinusculaAleatoria();
		}
		return res.toLowerCase();

	}
}
