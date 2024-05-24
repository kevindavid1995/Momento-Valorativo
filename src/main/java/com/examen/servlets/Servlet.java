package com.examen.servlets;

import com.examen.Conexion;
import com.examen.entidades.Curso;
import com.examen.entidades.Estudiante;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/students", "/students-average-age"})
public class Servlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getServletPath();
        List<Estudiante> estudiantes = getAllStudents();
        Gson gson = new Gson();
        String json;

        if ("/students".equals(path)) {
            json = gson.toJson(estudiantes);
        } else if ("/students-average-age".equals(path)) {
            json = gson.toJson(getStudentsAboveAverageAge(estudiantes));
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private List<Estudiante> getAllStudents() {
        Map<Integer, Estudiante> alumnosMap = new HashMap<>();
        String query = "SELECT st.id, st.nombre, st.apellido, st.edad, st.id as curso_id, co.nombre_curso, co.profesor, co.descripcion " +
                "FROM students st " +
                "LEFT JOIN students_courses sc ON st.id = sc.estudiante_id " +
                "LEFT JOIN courses co ON sc.curso_id = co.id";

        try (Connection connection = Conexion.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int alumnoId = resultSet.getInt("id");
                Estudiante estudiante = alumnosMap.getOrDefault(alumnoId, new Estudiante());

                if (estudiante.getId() == 0) {
                    estudiante.setId(alumnoId);
                    estudiante.setNombre(resultSet.getString("nombre"));
                    estudiante.setApellido(resultSet.getString("apellido"));
                    estudiante.setEdad(resultSet.getInt("edad"));
                    estudiante.setCursos(new ArrayList<>());
                    alumnosMap.put(alumnoId, estudiante);
                }

                int cursoId = resultSet.getInt("curso_id");
                if (cursoId > 0) {
                    Curso curso = new Curso();
                    curso.setId(cursoId);
                    curso.setNombreCurso(resultSet.getString("nombre_curso"));
                    curso.setProfesor(resultSet.getString("profesor"));
                    curso.setDescripcion(resultSet.getString("descripcion"));
                    estudiante.getCursos().add(curso);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(alumnosMap.values());
    }
    private List<Estudiante> getStudentsAboveAverageAge(List<Estudiante> estudiantes) {
        int totalEdad = 0;
        int count = 0;

        for (Estudiante estudiante : estudiantes) {
            totalEdad += estudiante.getEdad();
            count++;
        }

        int averageAge = totalEdad / count;
        List<Estudiante> estudianteAboveAverage = new ArrayList<>();

        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getEdad() > averageAge) {
                estudianteAboveAverage.add(estudiante);
            }
        }

        return estudianteAboveAverage;
    }
}
