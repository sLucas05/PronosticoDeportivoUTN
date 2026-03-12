package org.pintegradorg16c158.Predicciones;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

public class MainApp {
	@SuppressWarnings({ "unused", "resource" })
	public static void main(String[] args) throws IOException, CsvException, InterruptedException {
		
		String pathPartidos = "Partidos.csv";
        String pathCfg = "Configuracion.csv";

        try {
            new File(pathPartidos).createNewFile();
            new File(pathCfg).createNewFile();
        } catch (IOException e) {
            System.err.println("Error al crear los archivos: " + e.getMessage());
        }

		ArrayList<String> equipos = new ArrayList<String>(); 
		equipos.add("Argentina");
		equipos.add("Chile");
		equipos.add("Uruguay");
		equipos.add("Paraguay");
		equipos.add("Colombia");
		equipos.add("Ecuador");
		equipos.add("Brasil");
		equipos.add("Mexico");
		equipos.add("Portugal");
		equipos.add("Francia");
		equipos.add("Inglaterra");
		equipos.add("Alemania");
		equipos.add("Polonia");
		equipos.add("Marruecos");
		equipos.add("Croacia");
		equipos.add("Senegal");

		double Puntos = 0.00; 
		int Acerto = 0;
		int RondAcert = 0;
		int FasAcert = 0;
		int GolesAcert = 0;
		int PartTotal = 1;
		int PartNum = 1;
		int RondNum = 1;
		int FasNum = 1;
		int RAcertF = 0;
		int FAcertF = 0;
		String Jugar = "1";
		String equipoA, equipoB;
		String Prediccion = "";
		String GolesPredict = "0";
		String Separador = ",";
		Usuario Nombre = null;
		PrintWriter EscritorPart = null;
		PrintWriter NewCfg = null;
		Random random = new Random();
		Scanner scanner = new Scanner(System.in);

		FileWriter PuntajeClear = new FileWriter("Puntajes.txt");
		PuntajeClear.write("");
		PuntajeClear.close();

		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); 
		System.out.println("Argentina Programa 4.0 - Desarrollador Java Inicial (UTN)");
		System.out.println("Proyecto Integrador (Predicciones Deportivas)");
		System.out.println("Comision 158 Grupo 16");
		System.out.println("\nLos valores de prediccion son:");
		System.out.println("G1 (Gana el Equipo 1) / E (Empate) / G2 (Gana el Equipo 2)");
		System.out.println("\nIniciar programa de Predicciones Deportivas 'enter'...");
		scanner.nextLine();

		// ~ | Configuracion del programa | ~ //
		boolean Existe = false; 
		Scanner CfgScan = new Scanner(Paths.get(pathCfg));
		while (CfgScan.hasNextLine()) {
			String CfgExist = CfgScan.nextLine();
			if (CfgExist.contains("Database")) {
				Existe = true;
				break;
			}
		}
		String LoadCfg = "";
		String[] Cfg = {};
		if (Existe) {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			System.out.println("--------------------\nCargar configuracion| (SI/NO)\n--------------------");
			LoadCfg = scanner.nextLine();
		}
		if (LoadCfg.equalsIgnoreCase("No") || Existe == false) {
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			NewCfg = new PrintWriter(new FileWriter(pathCfg));
			NewCfg.print("Database=jdbc:mysql://localhost:3306/pronosticos" + Separador);
			System.out.println("--------------------------\nConfiguracion del programa|\n--------------------------\n");
			System.out.println("¿Cuantos partidos desea predecir?");
			String Parts = scanner.nextLine();
			NewCfg.print("Partidos=" + Parts + Separador);
			System.out.println("\n¿Cuantos partidos conformaran una ronda?");
			String PartxRond = scanner.nextLine();
			while (Integer.parseInt(Parts) <= Integer.parseInt(PartxRond)) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				System.err.println("ERROR: El numero debe ser inferior a " + Parts + "\n");
				System.out.println("¿Cuantos partidos conformaran una ronda?");
				PartxRond = scanner.nextLine();
			}
			NewCfg.print("PartXRond=" + PartxRond + Separador);
			System.out.println("\n¿Cuantas rondas conformaran una fase?");
			String RondxFas = scanner.nextLine();
			while (Integer.parseInt(PartxRond) >= Integer.parseInt(RondxFas)) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				System.err.println("ERROR: El numero debe ser superior a " + PartxRond + "\n");
				System.out.println("¿Cuantas rondas conformaran una fase?");
				RondxFas = scanner.nextLine();
			}
			NewCfg.print("RondXFas=" + RondxFas + Separador);
			System.out.println("\n¿Cuantos puntos desea que se otorguen al acertar una prediccion?");
			String PuntOtorg = scanner.nextLine();
			NewCfg.print("PtsAcert=" + PuntOtorg + Separador);
			System.out.println("\n¿Cuanto sera el bono que se otorgue al acertar los goles?");
			String PuntOtorgGol = scanner.nextLine();
			NewCfg.print("PtsAcertG=" + PuntOtorgGol + Separador);
			System.out.println("\n¿Cuanto sera el bono que se otorgue al acertar una ronda?");
			String PuntOtorgRond = scanner.nextLine();
			NewCfg.print("PtsAcertR=" + PuntOtorgRond + Separador);
			System.out.println("\n¿Cuanto sera el bono que se otorgue al acertar una fase?");
			String PuntOtorgFas = scanner.nextLine();
			NewCfg.print("PtsAcertF=" + PuntOtorgFas + Separador);
			System.out.println("\nUsuario de la base de datos:");
			String dbUser = scanner.nextLine();
			NewCfg.print("User="+ dbUser + Separador);
			System.out.println("\nContraseña de la base de datos:");
			String dbPass = scanner.nextLine();
			NewCfg.print("Password="+dbPass);
			System.out.println("");
			NewCfg.close();
		}
		CSVReader CfgRead = new CSVReader(new FileReader(pathCfg));
		List<String[]> Configs = CfgRead.readAll();
		for (String[] Config : Configs) {
			Cfg = Config;
		}
		CfgScan.close();

		// ~ | Vaciar base de datos | ~ //
		try { 
			Connection Conexion = DriverManager.getConnection(Cfg[0].replace("Database=", ""),
					Cfg[8].replace("User=", ""), Cfg[9].replace("Password=", ""));
			Statement Drop = Conexion.createStatement();
			Drop.executeUpdate("TRUNCATE TABLE prediccion");
			Drop.executeUpdate("TRUNCATE TABLE ranking");
			} catch (SQLException x) {
		}

		EscritorPart = new PrintWriter(new FileWriter(pathPartidos)); 

		// ~ | Configuración del jugador | ~ //
		while (Jugar.equalsIgnoreCase("1")) { 
			Jugar = "";
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			System.out.println("-------------------------\nConfiguracion del jugador|\n-------------------------\n");
			System.out.println("Escriba su nombre de usuario...");
			String NombreUsuario = scanner.nextLine();
			try {
				Connection ConexionUser = DriverManager.getConnection(
						Cfg[0].replace("Database=", ""),
						Cfg[8].replace("User=", ""), 
						Cfg[9].replace("Password=", ""));
				String GetUser = "SELECT * FROM prediccion WHERE Usuario = ?";
				PreparedStatement GetingUser = ConexionUser.prepareStatement(GetUser);
				GetingUser.setString(1, NombreUsuario);
				ResultSet GetedUser = GetingUser.executeQuery();
				while (GetedUser.next() == true) {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					System.err.println("ERROR: Usuario existente\n");
					System.out.println("-------------------------\nConfiguracion del jugador|\n-------------------------\n");
					System.out.println("Escriba su nombre de usuario...");
					NombreUsuario = scanner.nextLine();
					GetingUser.setString(1, NombreUsuario);
					GetedUser.close();
					GetedUser = GetingUser.executeQuery();
				}
				Nombre = new Usuario(NombreUsuario);
			} catch (SQLException x) {
				System.err.println(x.getMessage());
			}

			// ~ | Partidos y predicciones | ~ //
			while (PartTotal < Integer.parseInt(Cfg[1].replace("Partidos=", "")) + 1) { 
				int randomA = (random.nextInt(8));
				int randomB = (random.nextInt(8) + 8);
				equipoA = equipos.get(randomA);
				equipoB = equipos.get(randomB);
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					System.out.println("-----------------------------\nPreparando proximo partido...|\n-----------------------------");
					Thread.sleep(2000);
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					System.out.println("Partido [" + PartNum + "] // Ronda [" + RondNum + "] // Fase [" + FasNum + "]");
					System.out.println("\n(1)" + equipoA + " vs " + equipoB + "(2)\n");
					System.out.println("------------------------------\nConfiguracion de la prediccion|\n------------------------------\n");
					System.out.println("¿Cual es su prediccion?");
					Prediccion = scanner.nextLine();
					while (true) {
						if (Prediccion.equalsIgnoreCase("G1") || Prediccion.equalsIgnoreCase("G2")
								|| Prediccion.equalsIgnoreCase("E")) {
							break;
						}
						new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
						System.err.println("ERROR: Los valores son G1 / E / G2\n");
						System.out.println("¿Cual es su prediccion?");
						Prediccion = scanner.nextLine();
					}
					if (Prediccion.equalsIgnoreCase("G1") || Prediccion.equalsIgnoreCase("G2")) {
						System.out.println("\n¿Cuantos goles hace?");
						GolesPredict = scanner.nextLine();
						while (Integer.parseInt(GolesPredict) >= 4) {
							new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
							System.err.println("ERROR: El numero debe ser igual o inferior que 3\n");
							System.out.println("¿Cuantos goles hace?");
							GolesPredict = scanner.nextLine();
						}
					}
					Partido partido = new Partido(equipoA, equipoB);
					String ganador = partido.jugarPartido(equipoA, equipoB);
					EscritorPart.print(equipoA + Separador + partido.getGolesEquipo1() + Separador + partido.getGolesEquipo2()+ Separador + equipoB + Separador + "[Partido " + PartNum + Separador + "Ronda "+ RondNum + Separador + "Fase " + FasNum + Separador + NombreUsuario + "]\n");
					if (Prediccion.equalsIgnoreCase("G1")) {
						try {
							Connection Conexion = DriverManager.getConnection(
									Cfg[0].replace("Database=", ""),
									Cfg[8].replace("User=", ""), 
									Cfg[9].replace("Password=", ""));
							Statement Add = Conexion.createStatement();
							Add.executeUpdate("INSERT INTO prediccion (Usuario, EquipoA, Gana1, Empate, Gana2, EquipoB, Goles, Partido, Ronda, Fase) VALUES ('"+ NombreUsuario + "', '" + equipoA + "', 'X', '', '', '" + equipoB + "', "+ GolesPredict + ", " + PartNum + ", " + RondNum + ", " + FasNum + ")");
						} catch (SQLException x) {
						}
					} else if (Prediccion.equalsIgnoreCase("G2")) {
						try {
							Connection Conexion = DriverManager.getConnection(
									Cfg[0].replace("Database=", ""),
									Cfg[8].replace("User=", ""), 
									Cfg[9].replace("Password=", ""));
							Statement Add = Conexion.createStatement();
							Add.executeUpdate("INSERT INTO prediccion (Usuario, EquipoA, Gana1, Empate, Gana2, EquipoB, Goles, Partido, Ronda, Fase) VALUES ('"+ NombreUsuario + "', '" + equipoA + "', '', '', 'X', '" + equipoB + "', "+ GolesPredict + ", " + PartNum + ", " + RondNum + ", " + FasNum + ")");
						} catch (SQLException x) {
						}
					} else if (Prediccion.equalsIgnoreCase("E")) {
						try {
							Connection Conexion = DriverManager.getConnection(
									Cfg[0].replace("Database=", ""),
									Cfg[8].replace("User=", ""), 
									Cfg[9].replace("Password=", ""));
							Statement Add = Conexion.createStatement();
							Add.executeUpdate("INSERT INTO prediccion (Usuario, EquipoA, Gana1, Empate, Gana2, EquipoB, Goles, Partido, Ronda, Fase) VALUES ('"+ NombreUsuario + "', '" + equipoA + "', '', 'X', '', '" + equipoB+ "', null, " + PartNum + ", " + RondNum + ", " + FasNum + ")");
						} catch (SQLException x) {
						}
					}
					if (partido.getGolesEquipo2() > partido.getGolesEquipo1() && Prediccion.equalsIgnoreCase("G2")) {
						RondAcert++;
						if (RondAcert == Integer.parseInt(Cfg[2].replace("PartXRond=", ""))) {
							Puntos = Puntos + Double.parseDouble(Cfg[6].replace("PtsAcertR=", ""));
							RAcertF++;
							FasAcert++;
						}
						if (FasAcert == Integer.parseInt(Cfg[3].replace("RondXFas=", ""))) {
							Puntos = Puntos + Double.parseDouble(Cfg[7].replace("PtsAcertF=", ""));
							FAcertF++;
						}
					}
					if (partido.getGolesEquipo2() < partido.getGolesEquipo1() && Prediccion.equalsIgnoreCase("G1")) {
						RondAcert++;
						if (RondAcert == Integer.parseInt(Cfg[2].replace("PartXRond=", ""))) {
							Puntos = Puntos + Double.parseDouble(Cfg[6].replace("PtsAcertR=", ""));
							RAcertF++;
							FasAcert++;
						}
						if (FasAcert + 1 == Integer.parseInt(Cfg[3].replace("RondXFas=", ""))) {
							Puntos = Puntos + Double.parseDouble(Cfg[7].replace("PtsAcertF=", ""));
							FAcertF++;
						}
					}
					if (partido.getGolesEquipo2() == partido.getGolesEquipo1() && Prediccion.equalsIgnoreCase("E")) {
						RondAcert++;
						if (RondAcert == Integer.parseInt(Cfg[2].replace("PartXRond=", ""))) {
							Puntos = Puntos + Double.parseDouble(Cfg[6].replace("PtsAcertR=", ""));
							RAcertF++;
							FasAcert++;
						}
						if (FasAcert == Integer.parseInt(Cfg[3].replace("RondXFas=", ""))) {
							Puntos = Puntos + Double.parseDouble(Cfg[7].replace("PtsAcertF=", ""));
							FAcertF++;
						}
					}
					PartTotal++;
					if (PartNum == Integer.parseInt(Cfg[2].replace("PartXRond=", ""))) {
						PartNum = 1;
						RondAcert = 0;
						RondNum++;
					} else {
						PartNum++;
					}
					if (RondNum == Integer.parseInt(Cfg[3].replace("RondXFas=", "")) + 1) {
						RondNum = 1;
						FasAcert = 0;
						FasNum++;
					}
					if (partido.getGolesEquipo1() == Integer.parseInt(GolesPredict) && Prediccion.equalsIgnoreCase("G1") && partido.getGolesEquipo1() > partido.getGolesEquipo2()) {
						Puntos = Puntos + Double.parseDouble(Cfg[5].replace("PtsAcertG=", ""));
						GolesAcert++;
					}
					if (partido.getGolesEquipo2() == Integer.parseInt(GolesPredict) && Prediccion.equalsIgnoreCase("G2") && partido.getGolesEquipo2() > partido.getGolesEquipo1()) {
						Puntos = Puntos + Double.parseDouble(Cfg[5].replace("PtsAcertG=", ""));
						GolesAcert++;
					}
				} catch (Exception x) {
					x.printStackTrace();
				} finally {
				}
			}

			EscritorPart.close(); 
			EscritorPart = new PrintWriter(new FileWriter(pathPartidos, true)); 

			// ~ | Resultados | ~ //
			try { 
				CSVReader PartidosRead = new CSVReader(new FileReader(pathPartidos));
				try {
					List<String[]> filas = PartidosRead.readAll();
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					for (String[] fila : filas) {
						if (fila[7].equalsIgnoreCase(NombreUsuario + "]")) {
							equipoA = fila[0];
							equipoB = fila[3];
							int golesA = Integer.parseInt(fila[1]);
							int golesB = Integer.parseInt(fila[2]);
							Partido match = new Partido(equipoA, equipoB);
							match.setGolesEquipo1(golesA);
							match.setGolesEquipo2(golesB);
							try {
								Connection Conexion = DriverManager.getConnection(
										Cfg[0].replace("Database=", ""),
										Cfg[8].replace("User=", ""), 
										Cfg[9].replace("Password=", ""));
								PreparedStatement Geting = Conexion.prepareStatement("SELECT * FROM prediccion WHERE Usuario = '" + NombreUsuario+ "' AND Partido = " + fila[4].replace("[Partido ", "")+ " AND Ronda = " + fila[5].replace("Ronda ", "") + " AND Fase = "+ fila[6].replace("Fase ", "") + "");
								ResultSet Geted = Geting.executeQuery();
								while (Geted.next()) {
									String G1 = Geted.getString("Gana1");
									String E = Geted.getString("Empate");
									String G2 = Geted.getString("Gana2");
									{
										if (golesA > golesB) {
											System.out.println("[" + match.getGolesEquipo1() + "]" + match.getEquipo1()+ " - " + match.getEquipo2() + "[" + match.getGolesEquipo2()+ "] // Gano [" + match.getEquipo1() + "]");
											if (G1.equalsIgnoreCase("X")) {
												System.out.println("Tu prediccion fue correcta, +"+ Double.parseDouble(Cfg[4].replace("PtsAcert=", ""))+ " punto(s)");
												Puntos = Puntos + Double.parseDouble(Cfg[4].replace("PtsAcert=", ""));
												Acerto++;
											}
											System.out.println("");
										} else if (golesB > golesA) {
											System.out.println("[" + match.getGolesEquipo1() + "]" + match.getEquipo1()+ " - " + match.getEquipo2() + "[" + match.getGolesEquipo2()+ "] // Gano [" + match.getEquipo2() + "]");
											if (G2.equalsIgnoreCase("X")) {
												System.out.println("Tu prediccion fue correcta, +"+ Double.parseDouble(Cfg[4].replace("PtsAcert=", ""))+ " punto(s)");
												Puntos = Puntos + Double.parseDouble(Cfg[4].replace("PtsAcert=", ""));
												Acerto++;
											}
											System.out.println("");
										} else if (golesB == golesA) {
											System.out.println("[" + match.getGolesEquipo1() + "]" + match.getEquipo1()+ " - " + match.getEquipo2() + "[" + match.getGolesEquipo2()+ "] // Empate");
											if (E.equalsIgnoreCase("X")) {
												System.out.println("Tu prediccion fue correcta, +"+ Double.parseDouble(Cfg[4].replace("PtsAcert=", ""))+ " punto(s)");
												Puntos = Puntos + Double.parseDouble(Cfg[4].replace("PtsAcert=", ""));
												Acerto++;
											}
											System.out.println("");
										}
									}
								}
								Geted.close();
								Geting.close();
								Conexion.close();
							} catch (SQLException x) {
							}
						}
					}
				} catch (CsvValidationException z) {
					z.printStackTrace();
				} catch (CsvException y) {
					Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, y);
				}
			} catch (IOException x) {
				x.printStackTrace();
			}

			// ~ | Resultados finales y opciones | ~ //
			double Gacertados = Double.parseDouble(Cfg[5].replace("PtsAcertG=", "")) * GolesAcert; 																						
			double Racertadas = Double.parseDouble(Cfg[6].replace("PtsAcertR=", "")) * RAcertF;
			double Facertadas = Double.parseDouble(Cfg[7].replace("PtsAcertF=", "")) * FAcertF;
			if (GolesAcert > 0) {
				System.out.println("+" + Gacertados + " punto(s) por acertar la cantidad de goles en " + GolesAcert
						+ " partido(s)");
			}
			if (RAcertF > 0) {
				System.out.println(
						"+" + Racertadas + " punto(s) por acertar todos los partidos de " + RAcertF + " ronda(s)");
			}
			if (FAcertF > 0) {
				System.out.println(
						"+" + Facertadas + " punto(s) por acertar todas las rondas de " + FAcertF + " fase(s)");
			}
			if (FAcertF > 0 || RAcertF > 0 || GolesAcert > 0) {
				System.out.println("\n" + Nombre.getNombre() + " tus puntos ganados son: (" + Puntos
						+ ")\n---------------------------\n[1] Jugar con otro Usuario\n[2] Ver tabla de puntajes\n[3] Finalizar programa");
			} else {
				System.out.println(Nombre.getNombre() + " tus puntos ganados son: (" + Puntos
						+ ")\n---------------------------\n[1] Jugar con otro Usuario\n[2] Ver tabla de puntajes\n[3] Finalizar programa");
			}
			FileWriter Puntajes = new FileWriter("Puntajes.txt", true);
			BufferedWriter EscritorPuntajes = new BufferedWriter(Puntajes);
			EscritorPuntajes.write(Nombre.getNombre() + " | " + Puntos + " punto(s)\n----------------------------"
					+ "\n[Predicciones acertadas]: " + Acerto + "\n[Rondas acertadas]: " + RAcertF
					+ "\n[Fases acertadas]: " + FAcertF + "\n[Goles acertados]: " + GolesAcert + "\n\n");
			try {
				Connection ConexionRank = DriverManager.getConnection(Cfg[0].replace("Database=", ""),
						Cfg[8].replace("User=", ""), Cfg[9].replace("Password=", ""));
				Statement AddRank = ConexionRank.createStatement();
				AddRank.executeUpdate(
						"INSERT INTO ranking (Usuario, Puntos) VALUES ('" + NombreUsuario + "', " + Puntos + ")");
				;
			} catch (SQLException f) {
			}
			EscritorPuntajes.close();
			Jugar = scanner.nextLine();
			while (Integer.parseInt(Jugar) >= 4) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				System.err.println("ERROR: El numero debe ser igual o inferior que 3\n");
				System.out.println("[1] Jugar con otro Usuario\n[2] Ver tabla de puntajes\n[3] Finalizar programa");
				Jugar = scanner.nextLine();
			}
			if (Jugar.equalsIgnoreCase("1")) {
				Puntos = 0.0;
				Acerto = 0;
				RondAcert = 0;
				FasAcert = 0;
				RAcertF = 0;
				FAcertF = 0;
				GolesAcert = 0;
				PartTotal = 1;
				PartNum = 1;
				RondNum = 1;
				FasNum = 1;
			} else if (Jugar.equalsIgnoreCase("2")) {
				try {
					Connection Conexion = DriverManager.getConnection(
							Cfg[0].replace("Database=", ""),
							Cfg[8].replace("User=", ""), 
							Cfg[9].replace("Password=", ""));
					PreparedStatement Geting = Conexion.prepareStatement("SELECT * FROM ranking ORDER BY Puntos DESC");
					ResultSet Geted = Geting.executeQuery();
					int Rank = 0;
					while (Geted.next()) {
						Rank++;
						System.out.println("[" + Rank + "] " + Geted.getString("Usuario") + " ("+ Geted.getString("Puntos") + ")");
						FileWriter PuntajesRank = new FileWriter("Puntajes.txt", true);
						BufferedWriter EscritorPuntajesRank = new BufferedWriter(PuntajesRank);
						EscritorPuntajesRank.write("[" + Rank + "] " + Geted.getString("Usuario") + " ("+ Geted.getString("Puntos") + ")\n");
						EscritorPuntajesRank.close();
					}
				} catch (SQLException x) {
				}
				try {
					new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
					FileReader Tabla = new FileReader("Puntajes.txt");
					BufferedReader ReadTabla = new BufferedReader(Tabla);
					String Resultados;
					while ((Resultados = ReadTabla.readLine()) != null) {
						System.out.println(Resultados);
					}
					ReadTabla.close();
					System.out.println("\n(Archivo 'Puntajes.txt' generado exitosamente en la carpeta raiz de tu usuario)\n\n¡Gracias por utilizar nuestro programa de predicciones deportivas!\nCerrando...");
				} catch (IOException x) {
					x.printStackTrace();
				}
			} else if (Jugar.equalsIgnoreCase("3")) {
				new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
				try {
					Connection Conexion = DriverManager.getConnection(
							Cfg[0].replace("Database=", ""),
							Cfg[8].replace("User=", ""), 
							Cfg[9].replace("Password=", ""));
					PreparedStatement Geting = Conexion.prepareStatement("SELECT * FROM ranking ORDER BY Puntos DESC");
					ResultSet Geted = Geting.executeQuery();
					int Rank = 0;
					while (Geted.next()) {
						Rank++;
						FileWriter PuntajesRank = new FileWriter("Puntajes.txt", true);
						BufferedWriter EscritorPuntajesRank = new BufferedWriter(PuntajesRank);
						EscritorPuntajesRank.write("[" + Rank + "] " + Geted.getString("Usuario") + " ("+ Geted.getString("Puntos") + ")\n");
						EscritorPuntajesRank.close();
					}
				} catch (SQLException x) {
				}
				System.out.println("(Archivo 'Puntajes.txt' generado exitosamente en la carpeta raiz de tu usuario)\n\n¡Gracias por utilizar nuestro programa de predicciones deportivas!\nCerrando...");
			}
		}
	}
}