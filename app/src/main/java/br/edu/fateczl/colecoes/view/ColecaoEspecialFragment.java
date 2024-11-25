package br.edu.fateczl.colecoes.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.fateczl.colecoes.R;
import br.edu.fateczl.colecoes.controller.ColecaoEspecialController;
import br.edu.fateczl.colecoes.model.ColecaoEspecial;
import br.edu.fateczl.colecoes.persistence.ColecaoEspecialDAO;

import java.sql.SQLException;
import java.util.List;

public class ColecaoEspecialFragment extends Fragment {
    private View view;
    private EditText editTextColecaoId;
    private EditText editTextColecaoNome;
    private EditText editTextColecaoPreco;
    private EditText editTextColecaoCategoria;
    private CheckBox checkBoxColecaoEdicaoLimitada;
    private TextView textViewColecaoOutput;
    private ColecaoEspecialController colecaoEspecialController;

    public ColecaoEspecialFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_colecao_especial, container, false);

        editTextColecaoId = view.findViewById(R.id.editTextColecaoId);
        editTextColecaoNome = view.findViewById(R.id.editTextColecaoNome);
        editTextColecaoPreco = view.findViewById(R.id.editTextColecaoPreco);
        editTextColecaoCategoria = view.findViewById(R.id.editTextColecaoCategoria);
        checkBoxColecaoEdicaoLimitada = view.findViewById(R.id.checkBoxColecaoEdicaoLimitada);
        textViewColecaoOutput = view.findViewById(R.id.textViewColecaoOutput);
        textViewColecaoOutput.setMovementMethod(new ScrollingMovementMethod());

        colecaoEspecialController = new ColecaoEspecialController(new ColecaoEspecialDAO(view.getContext()));

        view.findViewById(R.id.buttonColecaoBuscar).setOnClickListener(search -> searchEntry());
        view.findViewById(R.id.buttonColecaoCadastrar).setOnClickListener(register -> registerEntry());
        view.findViewById(R.id.buttonColecaoAtualizar).setOnClickListener(update -> updateEntry());
        view.findViewById(R.id.buttonColecaoRemover).setOnClickListener(remove -> removeEntry());
        view.findViewById(R.id.buttonColecaoListar).setOnClickListener(list -> listEntry());

        return view;
    }

    private void searchEntry() {
        ColecaoEspecial colecao;

        try {
            if (editTextColecaoId.length() == 0) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            colecao = colecaoEspecialController.searchEntry(new ColecaoEspecial(
                    Integer.parseInt(editTextColecaoId.getText().toString()),
                    null, 0, null, false));

            if (colecao.getNome() != null) {
                setColecaoValues(colecao);
            } else {
                Toast.makeText(
                        view.getContext(), "Coleção não encontrada", Toast.LENGTH_LONG).show();

                clearColecaoValues();
            }
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerEntry() {
        try {
            colecaoEspecialController.registerEntry(getColecaoValues());

            Toast.makeText(
                    view.getContext(), "Coleção cadastrada com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearColecaoValues();
    }

    private void updateEntry() {
        try {
            colecaoEspecialController.updateEntry(getColecaoValues());

            Toast.makeText(
                    view.getContext(), "Coleção atualizada com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearColecaoValues();
    }

    private void removeEntry() {
        try {
            if (editTextColecaoId.length() == 0) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            colecaoEspecialController.removeEntry(new ColecaoEspecial(
                    Integer.parseInt(editTextColecaoId.getText().toString()),
                    null, 0, null, false));

            Toast.makeText(
                    view.getContext(), "Coleção removida com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearColecaoValues();
    }

    private void listEntry() {
        try {
            List<ColecaoEspecial> colecoes = colecaoEspecialController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (ColecaoEspecial colecao : colecoes) {
                stringBuffer.append(colecao.toString()).append("\n");
            }

            textViewColecaoOutput.setText(stringBuffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearColecaoValues();
    }

    private ColecaoEspecial getColecaoValues() throws IllegalArgumentException {
        if (!isColecaoValuesValid()) {
            throw new IllegalArgumentException("Entrada inválida");
        }

        return new ColecaoEspecial(
                Integer.parseInt(editTextColecaoId.getText().toString()),
                editTextColecaoNome.getText().toString(),
                Double.parseDouble(editTextColecaoPreco.getText().toString()),
                editTextColecaoCategoria.getText().toString(),
                checkBoxColecaoEdicaoLimitada.isChecked()
        );
    }

    private boolean isColecaoValuesValid() {
        return !(editTextColecaoId.length() == 0 ||
                editTextColecaoNome.length() == 0 ||
                editTextColecaoPreco.length() == 0 ||
                editTextColecaoCategoria.length() == 0);
    }

    private void setColecaoValues(ColecaoEspecial colecao) {
        editTextColecaoId.setText(String.valueOf(colecao.getId()));
        editTextColecaoNome.setText(colecao.getNome());
        editTextColecaoPreco.setText(String.valueOf(colecao.getPreco()));
        editTextColecaoCategoria.setText(colecao.getCategoria());
        checkBoxColecaoEdicaoLimitada.setChecked(colecao.isEdicaoLimitada());
    }

    private void clearColecaoValues() {
        editTextColecaoId.setText("");
        editTextColecaoNome.setText("");
        editTextColecaoPreco.setText("");
        editTextColecaoCategoria.setText("");
        checkBoxColecaoEdicaoLimitada.setChecked(false);
    }
}
