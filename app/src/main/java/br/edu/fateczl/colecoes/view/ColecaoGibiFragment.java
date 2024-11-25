package br.edu.fateczl.colecoes.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.fateczl.colecoes.R;
import br.edu.fateczl.colecoes.controller.ColecaoGibiController;
import br.edu.fateczl.colecoes.model.ColecaoGibi;
import br.edu.fateczl.colecoes.persistence.ColecaoGibiDAO;

import java.sql.SQLException;
import java.util.List;

public class ColecaoGibiFragment extends Fragment {
    private View view;
    private EditText editTextColecaoId;
    private EditText editTextColecaoNome;
    private EditText editTextColecaoPreco;
    private EditText editTextColecaoEditora;
    private TextView textViewColecaoOutput;
    private ColecaoGibiController colecaoGibiController;

    public ColecaoGibiFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_colecao_gibi, container, false);

        editTextColecaoId = view.findViewById(R.id.editTextColecaoId);
        editTextColecaoNome = view.findViewById(R.id.editTextColecaoNome);
        editTextColecaoPreco = view.findViewById(R.id.editTextColecaoPreco);
        editTextColecaoEditora = view.findViewById(R.id.editTextColecaoEditora);
        textViewColecaoOutput = view.findViewById(R.id.textViewColecaoOutput);
        textViewColecaoOutput.setMovementMethod(new ScrollingMovementMethod());

        colecaoGibiController = new ColecaoGibiController(new ColecaoGibiDAO(view.getContext()));

        view.findViewById(R.id.buttonColecaoBuscar).setOnClickListener(search -> searchEntry());
        view.findViewById(R.id.buttonColecaoCadastrar).setOnClickListener(register -> registerEntry());
        view.findViewById(R.id.buttonColecaoAtualizar).setOnClickListener(update -> updateEntry());
        view.findViewById(R.id.buttonColecaoRemover).setOnClickListener(remove -> removeEntry());
        view.findViewById(R.id.buttonColecaoListar).setOnClickListener(list -> listEntry());

        return view;
    }

    private void searchEntry() {
        ColecaoGibi colecaoGibi;

        try {
            if (editTextColecaoId.length() == 0) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            colecaoGibi = colecaoGibiController.searchEntry(new ColecaoGibi(
                    Integer.parseInt(editTextColecaoId.getText().toString()),
                    null, 0, null));

            if (colecaoGibi.getNome() != null) {
                setColecaoValues(colecaoGibi);
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
            colecaoGibiController.registerEntry(getColecaoValues());

            Toast.makeText(
                    view.getContext(), "Coleção cadastrada com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearColecaoValues();
    }

    private void updateEntry() {
        try {
            colecaoGibiController.updateEntry(getColecaoValues());

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

            colecaoGibiController.removeEntry(new ColecaoGibi(
                    Integer.parseInt(editTextColecaoId.getText().toString()),
                    null, 0, null));

            Toast.makeText(
                    view.getContext(), "Coleção removida com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearColecaoValues();
    }

    private void listEntry() {
        try {
            List<ColecaoGibi> colecoes = colecaoGibiController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (ColecaoGibi colecao : colecoes) {
                stringBuffer.append(colecao.toString()).append("\n");
            }

            textViewColecaoOutput.setText(stringBuffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearColecaoValues();
    }

    private ColecaoGibi getColecaoValues() throws IllegalArgumentException {
        if (!isColecaoValuesValid()) {
            throw new IllegalArgumentException("Entrada inválida");
        }

        return new ColecaoGibi(
                Integer.parseInt(editTextColecaoId.getText().toString()),
                editTextColecaoNome.getText().toString(),
                Double.parseDouble(editTextColecaoPreco.getText().toString()),
                editTextColecaoEditora.getText().toString()
        );
    }

    private boolean isColecaoValuesValid() {
        return !(editTextColecaoId.length() == 0 ||
                editTextColecaoNome.length() == 0 ||
                editTextColecaoPreco.length() == 0 ||
                editTextColecaoEditora.length() == 0);
    }

    private void setColecaoValues(ColecaoGibi colecao) {
        editTextColecaoId.setText(String.valueOf(colecao.getId()));
        editTextColecaoNome.setText(colecao.getNome());
        editTextColecaoPreco.setText(String.valueOf(colecao.getPreco()));
        editTextColecaoEditora.setText(colecao.getEditora());
    }

    private void clearColecaoValues() {
        editTextColecaoId.setText("");
        editTextColecaoNome.setText("");
        editTextColecaoPreco.setText("");
        editTextColecaoEditora.setText("");
    }
}
