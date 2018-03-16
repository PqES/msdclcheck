package visualization;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import core.CommunicateDefinition;
import core.MicroserviceDefinition;
import core.MicroservicesSystem;
import exception.MsDCLException;

public class MatrizVisualization {

	ArrayList<DependencyCommunication> communications = new ArrayList<DependencyCommunication>();

	public MatrizVisualization() {	}

	public String[][] updateMatriz(String[][] violates) {
		int i = 0;
		int j = 0;

		for (i = 0; i < violates.length; i++) {
			for (j = 0; j < violates[i].length; j++) {
				if (violates[i][j] == null) {
					violates[i][j] = "\t";
				}
			}

		}
		return violates;

	}

	public String createHtmlTable(String[][] informations) throws IOException {
		StringBuilder htmlBuilder = new StringBuilder();
		htmlBuilder.append("<html>");
		htmlBuilder.append(
				"<head><link rel=\"stylesheet\" type=\"text/css\" href=\"visualizacao.css\"><title>Architectural Visualization</title></head>");
		htmlBuilder.append("<body><p>Microservices</p>");
		htmlBuilder.append("<div class=\"divTable\">");
		htmlBuilder.append("<div class=\"divTableBody\">");
		for (String[] microservices : informations) {
			htmlBuilder.append("<div class=\"divTableRow\">");
			for (String ms : microservices) {
				htmlBuilder.append(
						"<div class=\"divTableCell\" style=\"background-color: white;\">&nbsp;" + ms + "</div>");

			}
			htmlBuilder.append("</div>");
		}
		htmlBuilder.append("</div>");
		htmlBuilder.append("</div>");
		htmlBuilder.append("</body>");
		htmlBuilder.append("</html>");

		String html = htmlBuilder.toString();
		return html;
	}

	public String[][] createMatriz(MicroservicesSystem system) throws IOException, MsDCLException {

		int tam = system.getMicroservices().size() + 1;
		String[][] informations = new String[tam][tam];

		informations[0][0] = new String("Microservices");
		int i = 1;
		int j = 1;
		for (MicroserviceDefinition m : system.getMicroservices()) {

			communications.add(new DependencyCommunication(0, i, m.getName()));
			communications.add(new DependencyCommunication(j, 0, m.getName()));
			informations[0][i] = new String("" + i);
			informations[j][0] = new String(i + " - " + m.getName());

			if (i == j) {
				informations[i][j] = new String("--");
			}
			i++;
			j++;

		}

		return informations;
	}

	

	public String[][] addCommunications(MicroservicesSystem system) throws IOException, MsDCLException {

		String[][] informations = createMatriz(system);

		for (MicroserviceDefinition m : system.getMicroservices()) {
			for (CommunicateDefinition c : system.getCommunications(m)) {
				String origem = c.getMicroserviceOrigin();
				String destino = c.getMicroserviceDestin();
				int pos_j = getPositionsY(origem); 
				int pos_i = getPositionsX(destino);

				int valor = 0;
				String v = informations[pos_i][pos_j]; 
				if (v != null) {
					DependencyCommunication dep = getInformations(pos_i, pos_j);
					valor = Integer.parseInt(v);
					valor += 1;
					dep.setValue(valor);
					informations[pos_i][pos_j] = "" + valor;

				} else {
					valor = 1;
					informations[pos_i][pos_j] = "" + valor;
					communications.add(new DependencyCommunication(pos_i, pos_j, origem, destino, valor));
				}

			}

		}

		return informations;

	}

	private DependencyCommunication getInformations(int pos_i, int pos_j) {
		for (DependencyCommunication comm : communications) {
			if ((comm.getPosicao_i() == pos_i) && (comm.getPosicao_j() == pos_j)) {
				return comm;
			}
		}
		return null;

	}

	public int getPositionsX(String name) {
		for (DependencyCommunication i : communications) {
			if (i.getMsName().equals(name)) {
				return i.getPosicao_j();
			}
		}
		return -1;

	}

	public int getPositionsY(String name) {
		for (DependencyCommunication i : communications) {
			if (i.getMsName().equals(name)) {
				int pos_J = i.getPosicao_j();
				return pos_J;
			}
		}
		return -1;

	}

	public ArrayList<DependencyCommunication> getCommunications() {
		return communications;
	}
}
