package controllers;

import org.controlsfx.control.Notifications;

import classes.Empresas_Pessoas;
import classes.Rebanhos;
import classes.Usuarios;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import utils.DAOHibernate;

public class cadastroRebanhoController {

	@FXML
	private Button btnSalvar;

	@FXML
	private Button btnCancelar;

	@FXML
	private TextField txtNome;

	@FXML
	private TextArea txtADescricao;

	private Usuarios user;

	public Usuarios getUser() {
		return user;
	}

	public void setUser(Usuarios user) {
		this.user = user;
	}

	@FXML
	public void salvar() throws Exception {

		String nome = txtNome.getText();
		String descricao = txtADescricao.getText();
		Empresas_Pessoas empresa = user.getIdEmpresas_Pessoa();

		DAOHibernate<Rebanhos> daoR = new DAOHibernate<Rebanhos>(Rebanhos.class);

		Rebanhos rebanho = new Rebanhos(nome, descricao, empresa);

		if (txtNome.getText().isEmpty()) {
			Notifications.create().title("Cadastro de Rebanhos").text("O rebanho precisa de um nome!").showError();
		} else {

			daoR.beginTransaction().save(rebanho).commitTransaction().closeAll();
			Notifications.create().title("Alerta").text("Rebanho " + rebanho.getNome() + " Adicionado com sucesso! ")
					.showConfirm();
			txtNome.clear();
			txtADescricao.clear();
		}
	}

	@FXML
	public void cancelar() {

		Stage currentStage = (Stage) btnCancelar.getScene().getWindow();
		currentStage.close();
	}

}
