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
import br.edu.fateczl.colecoes.controller.ClienteController;
import br.edu.fateczl.colecoes.model.Cliente;
import br.edu.fateczl.colecoes.persistence.ClienteDAO;

import java.sql.SQLException;
import java.util.List;

public class ClienteFragment extends Fragment {
    private View view;
    private EditText editTextClienteId;
    private EditText editTextClienteCNPJ;
    private EditText editTextClienteNome;
    private EditText editTextClienteEndereco;
    private EditText editTextClienteTelefone;
    private TextView textViewClienteOutput;
    private ClienteController clienteController;

    public ClienteFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cliente, container, false);

        editTextClienteId = view.findViewById(R.id.editTextClienteId);
        editTextClienteCNPJ = view.findViewById(R.id.editTextClienteCNPJ);
        editTextClienteNome = view.findViewById(R.id.editTextClienteNome);
        editTextClienteEndereco = view.findViewById(R.id.editTextClienteEndereco);
        editTextClienteTelefone = view.findViewById(R.id.editTextClienteTelefone);
        textViewClienteOutput = view.findViewById(R.id.textViewClienteOutput);
        textViewClienteOutput.setMovementMethod(new ScrollingMovementMethod());

        clienteController = new ClienteController(new ClienteDAO(view.getContext()));

        view.findViewById(R.id.buttonClienteBuscar).setOnClickListener(search -> searchEntry());
        view.findViewById(R.id.buttonClienteCadastrar).setOnClickListener(register -> registerEntry());
        view.findViewById(R.id.buttonClienteAtualizar).setOnClickListener(update -> updateEntry());
        view.findViewById(R.id.buttonClienteRemover).setOnClickListener(remove -> removeEntry());
        view.findViewById(R.id.buttonClienteListar).setOnClickListener(list -> listEntry());

        return view;
    }

    private void searchEntry() {
        Cliente cliente;

        try {
            if (editTextClienteId.length() == 0) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            cliente = clienteController.searchEntry(new Cliente(
                    Integer.parseInt(editTextClienteId.getText().toString()),
                    null, null, null, null));

            if (cliente.getNome() != null) {
                setClienteValues(cliente);
            } else {
                Toast.makeText(
                        view.getContext(), "Cliente não encontrado", Toast.LENGTH_LONG).show();

                clearClienteValues();
            }
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void registerEntry() {
        try {
            clienteController.registerEntry(getClienteValues());

            Toast.makeText(
                    view.getContext(), "Cliente cadastrado com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearClienteValues();
    }

    private void updateEntry() {
        try {
            clienteController.updateEntry(getClienteValues());

            Toast.makeText(
                    view.getContext(), "Cliente atualizado com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearClienteValues();
    }

    private void removeEntry() {
        try {
            if (editTextClienteId.length() == 0) {
                throw new IllegalArgumentException("Entrada inválida");
            }

            clienteController.removeEntry(new Cliente(
                    Integer.parseInt(editTextClienteId.getText().toString()),
                    null, null, null, null));

            Toast.makeText(
                    view.getContext(), "Cliente removido com sucesso", Toast.LENGTH_LONG).show();
        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearClienteValues();
    }

    private void listEntry() {
        try {
            List<Cliente> clientes = clienteController.listEntry();

            StringBuilder stringBuffer = new StringBuilder();

            for (Cliente cliente : clientes) {
                stringBuffer.append(cliente.toString()).append("\n");
            }

            textViewClienteOutput.setText(stringBuffer.toString());
        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        clearClienteValues();
    }

    private Cliente getClienteValues() throws IllegalArgumentException {
        if (!isClienteValuesValid()) {
            throw new IllegalArgumentException("Entrada inválida");
        }

        return new Cliente(
                Integer.parseInt(editTextClienteId.getText().toString()),
                editTextClienteCNPJ.getText().toString(),
                editTextClienteNome.getText().toString(),
                editTextClienteEndereco.getText().toString(),
                editTextClienteTelefone.getText().toString()
        );
    }

    private boolean isClienteValuesValid() {
        return !(editTextClienteId.length() == 0 ||
                editTextClienteCNPJ.length() == 0 ||
                editTextClienteNome.length() == 0 ||
                editTextClienteEndereco.length() == 0 ||
                editTextClienteTelefone.length() == 0);
    }

    private void setClienteValues(Cliente cliente) {
        editTextClienteId.setText(String.valueOf(cliente.getIdCliente()));
        editTextClienteCNPJ.setText(cliente.getCnpj());
        editTextClienteNome.setText(cliente.getNome());
        editTextClienteEndereco.setText(cliente.getEndereco());
        editTextClienteTelefone.setText(cliente.getTelefone());
    }

    private void clearClienteValues() {
        editTextClienteId.setText("");
        editTextClienteCNPJ.setText("");
        editTextClienteNome.setText("");
        editTextClienteEndereco.setText("");
        editTextClienteTelefone.setText("");
    }
}
