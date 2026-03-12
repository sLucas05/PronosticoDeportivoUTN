package org.pintegradorg16c158.Predicciones;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;

import org.junit.Test;

import com.opencsv.CSVReader;

public class AppTest {
	@Test
	public void VerificacionEnteros() throws IOException {
		InputStream DataInput = getClass().getResourceAsStream("/Partidos.csv");
		BufferedReader DataVerificacion = new BufferedReader(new InputStreamReader(DataInput));
		String Linea;
		int NumLinea = 0;
		System.out.println("\n[Verificacion de enteros]");
		while ((Linea = DataVerificacion.readLine()) != null) {
			NumLinea++;
			String[] Campo = Arrays.copyOf(Linea.split(","), 4);
			assertEquals("Numero de campos no valido " + NumLinea, 4, Campo.length);
			try {
				Integer.parseInt(Campo[1]);
				Integer.parseInt(Campo[2]);
				System.out.println("Numero: " + Campo[1] + " ✓\nNumero: " + Campo[2] + " ✓");
			} catch (NumberFormatException x) {
				fail("El formato del numero no es el correcto: " + NumLinea);
			}
		}
		DataVerificacion.close();
	}

	@Test
	public void ComprobacionRondas() throws IOException {
		try (CSVReader RoundRead = new CSVReader(new FileReader("Partidos.csv"))) {
			String[] Rounds;
			int Puntos = 0;
			System.out.println("\n[Comprobacion de rondas]");
			while ((Rounds = RoundRead.readNext()) != null) {
				for (String Round : Rounds) {
					if (Round.contains("Ronda 2") || Round.contains("Ronda 1")) {
						Connection ConexionRound = DriverManager.getConnection("jdbc:mysql://localhost:3306/pronostico", "user", "password");
						PreparedStatement GetingRound = ConexionRound.prepareStatement("SELECT * FROM prediccion WHERE Usuario = 'Lucas' AND Partido = "+ Rounds[4].replace("[Partido ", "") + " AND Ronda = "+ Rounds[5].replace("Ronda ", "") + " AND Fase = "+ Rounds[6].replace("Fase ", "") + "");
						ResultSet GetedRound = GetingRound.executeQuery();
						while (GetedRound.next()) {
							String G1Round = GetedRound.getString("Gana1");
							String ERound = GetedRound.getString("Empate");
							String G2Round = GetedRound.getString("Gana2");
							if (Integer.parseInt(Rounds[1]) > Integer.parseInt(Rounds[2]) && G1Round.equalsIgnoreCase("X")) {
								Puntos++;
							}
							if (Integer.parseInt(Rounds[1]) < Integer.parseInt(Rounds[2]) && G2Round.equalsIgnoreCase("X")) {
								Puntos++;
							}
							if (Integer.parseInt(Rounds[1]) == Integer.parseInt(Rounds[2]) && ERound.equalsIgnoreCase("X")) {
								Puntos++;
							}
							break;
						}
					}
				}
			}
			System.out.println(Puntos + " Punto(s) totales ganados en la prediccion de 2 rondas consecutivas");
		} catch (Exception z) {
		}
	}
}
