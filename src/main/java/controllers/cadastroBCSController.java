package controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.controlsfx.control.Notifications;

import classes.BCS;
import classes.Bovinos;
import classes.Usuarios;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.DAOHibernate;

public class cadastroBCSController {
	
	@FXML
	private TextField txtIndiceBCS;
	
	@FXML
	private DatePicker dateData;
	
	@FXML
	private ComboBox<String> comboBovino;
	
	@FXML
	private Button btnSalvar;
	
	@FXML
	private Button btnCancelar;
	
	private Usuarios user;
	
	public Usuarios getUser() {
		return user;
	}



	public void setUser(Usuarios user) {
		this.user = user;
	}

	private Date localDateToDate(LocalDate data) {
		ZoneId zoneidDefault = ZoneId.systemDefault();

		return Date.from(data.atStartOfDay(zoneidDefault).toInstant());
	}
	
	public void populateCombo() {
		
		DAOHibernate<Bovinos> daoB = new DAOHibernate<>(Bovinos.class);
		
		List<Bovinos> query = daoB.getAllByNamedQuery("selectBovinobyEmpresa", "empresa", 
				user.getIdEmpresas_Pessoa());
		
		comboBovino.getItems().add("Selecione...");
		for (Bovinos bovinos : query) {
			comboBovino.getItems().add(bovinos.getNome());
		}
		
	}
	
	private Bovinos findBovino(String bovinoNome) {
		
		DAOHibernate<Bovinos> daoB = new DAOHibernate<>(Bovinos.class);
		Bovinos bovino = daoB.getFirst("selectBovinobyNomeEmpresa", "nome", bovinoNome, "empresa", user.getIdEmpresas_Pessoa());
		
		if (bovino == null) {
			return null;
		}else {
			return bovino;
		}
	}
	
	@FXML
	public void salvar() {
		Double indiceBCS = Double.parseDouble(txtIndiceBCS.getText()); 
		Date dataBCS = localDateToDate(dateData.getValue());
		Bovinos bovino = findBovino(comboBovino.getValue());
		
		DAOHibernate<BCS> daoBCS = new DAOHibernate<>(BCS.class);
		BCS bcsObj = new BCS(bovino, dataBCS, indiceBCS);
		
		daoBCS.beginTransaction().save(bcsObj).commitTransaction().closeAll();
		
		Notifications.create().title("Alerta").text("Novo BCS adicionado com sucesso!")
		.showConfirm();
		
		txtIndiceBCS.clear();
		comboBovino.setValue(null);
		
	}
	
	@FXML
	public void cancelar() {
		
		Stage window = (Stage) btnCancelar.getScene().getWindow();
		window.close();
	}
	
}
