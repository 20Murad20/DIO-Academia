package academia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class AcademiaApp {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/academia";
    private static final String USER = "root";
    private static final String PASSWORD = "password";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
            System.out.println("Conectado ao banco de dados.");

            System.out.print("Digite o nome do aluno: ");
            String nome = scanner.nextLine();
            System.out.print("Digite a idade do aluno: ");
            int idade = scanner.nextInt();
            scanner.nextLine(); 

            String sqlInsertAluno = "INSERT INTO alunos (nome, idade) VALUES (?, ?)";
            int alunoId;
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertAluno, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, nome);
                pstmt.setInt(2, idade);
                pstmt.executeUpdate();

                try (var rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        alunoId = rs.getInt(1);

                        System.out.print("Digite a descrição do treinamento: ");
                        String descricao = scanner.nextLine();

                        String sqlInsertTreinamento = "INSERT INTO treinamentos (aluno_id, descricao) VALUES (?, ?)";
                        try (PreparedStatement pstmtTreinamento = conn.prepareStatement(sqlInsertTreinamento)) {
                            pstmtTreinamento.setInt(1, alunoId);
                            pstmtTreinamento.setString(2, descricao);
                            pstmtTreinamento.executeUpdate();
                            System.out.println("Dados do treinamento inseridos com sucesso.");
                        }

                        System.out.print("Digite o peso do aluno (kg): ");
                        double peso = scanner.nextDouble();
                        System.out.print("Digite a altura do aluno (m): ");
                        double altura = scanner.nextDouble();
                        scanner.nextLine(); 
                        System.out.print("Digite a data da avaliação (yyyy-mm-dd): ");
                        String dataAvaliacao = scanner.nextLine();

                        String sqlInsertAvaliacao = "INSERT INTO avaliacoes (aluno_id, peso, altura, data_avaliacao) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement pstmtAvaliacao = conn.prepareStatement(sqlInsertAvaliacao)) {
                            pstmtAvaliacao.setInt(1, alunoId);
                            pstmtAvaliacao.setDouble(2, peso);
                            pstmtAvaliacao.setDouble(3, altura);
                            pstmtAvaliacao.setString(4, dataAvaliacao);
                            pstmtAvaliacao.executeUpdate();
                            System.out.println("Dados da avaliação física inseridos com sucesso.");
                        }

                        System.out.print("Digite a data da matrícula (yyyy-mm-dd): ");
                        String dataMatricula = scanner.nextLine();
                        System.out.print("Digite o curso do aluno: ");
                        String curso = scanner.nextLine();

                        String sqlInsertMatricula = "INSERT INTO matriculas (aluno_id, data_matricula, curso) VALUES (?, ?, ?)";
                        try (PreparedStatement pstmtMatricula = conn.prepareStatement(sqlInsertMatricula)) {
                            pstmtMatricula.setInt(1, alunoId);
                            pstmtMatricula.setString(2, dataMatricula);
                            pstmtMatricula.setString(3, curso);
                            pstmtMatricula.executeUpdate();
                            System.out.println("Dados da matrícula inseridos com sucesso.");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
