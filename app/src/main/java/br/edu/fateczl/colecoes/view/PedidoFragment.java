package br.edu.fateczl.colecoes.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import br.edu.fateczl.colecoes.R;
import br.edu.fateczl.colecoes.controller.ClienteController;
import br.edu.fateczl.colecoes.controller.GibiController;
import br.edu.fateczl.colecoes.controller.PedidoController;
import br.edu.fateczl.colecoes.model.Cliente;
import br.edu.fateczl.colecoes.model.Gibi;
import br.edu.fateczl.colecoes.model.ItemPedido;
import br.edu.fateczl.colecoes.model.Pedido;
import br.edu.fateczl.colecoes.persistence.ClienteDAO;
import br.edu.fateczl.colecoes.persistence.GibiDAO;
import br.edu.fateczl.colecoes.persistence.PedidoDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoFragment extends Fragment {
    private View view;
    private EditText editTextPedidoId;
    private Spinner spinnerCliente;
    private Spinner spinnerGibi;
    private EditText editTextQuantidade;
    private TextView textViewTotalPedido;
    private ClienteController clienteController;
    private GibiController gibiController;
    private PedidoController pedidoController;
    private List<Cliente> clientes;
    private List<Gibi> gibis;
    private Pedido pedido;

    public PedidoFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pedido, container, false);

        editTextPedidoId = view.findViewById(R.id.editTextPedidoId);
        spinnerCliente = view.findViewById(R.id.spinnerCliente);
        spinnerGibi = view.findViewById(R.id.spinnerGibi);
        editTextQuantidade = view.findViewById(R.id.editTextQuantidade);
        textViewTotalPedido = view.findViewById(R.id.textViewTotalPedido);
        textViewTotalPedido.setMovementMethod(new ScrollingMovementMethod());

        clienteController = new ClienteController(new ClienteDAO(this.getContext()));
        gibiController = new GibiController(new GibiDAO(this.getContext()));
        pedidoController = new PedidoController(new PedidoDAO(this.getContext()));

        carregarClientes();
        carregarGibis();

        view.findViewById(R.id.buttonNovoPedido).setOnClickListener(novoPedido -> criarPedido());
        view.findViewById(R.id.buttonAdicionarItem).setOnClickListener(adicionar -> adicionarItem());
        view.findViewById(R.id.buttonLimparPedido).setOnClickListener(limpar -> limparPedido());
        view.findViewById(R.id.buttonFinalizarPedido).setOnClickListener(finalizar -> registrarPedido());

        return view;
    }

    private void carregarClientes() {
        Cliente clientePadrao = new Cliente(0, "Selecione um cliente", null, null, null);
        List<Cliente> listaClientes = new ArrayList<>();

        try {
            clientes = clienteController.listarTodos();
            listaClientes.add(0, clientePadrao);
            listaClientes.addAll(clientes);

            ArrayAdapter<Cliente> adapter = new ArrayAdapter<>(
                    view.getContext(), android.R.layout.simple_spinner_item, listaClientes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCliente.setAdapter(adapter);

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void carregarGibis() {
        Gibi gibiPadrao = new Gibi(0, "Selecione um gibi", 0.0, "Padrão");
        List<Gibi> listaGibis = new ArrayList<>();

        try {
            gibis = gibiController.listarTodos();
            listaGibis.add(gibiPadrao);
            listaGibis.addAll(gibis);

            ArrayAdapter<Gibi> adapter = new ArrayAdapter<>(
                    view.getContext(), android.R.layout.simple_spinner_item, listaGibis);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGibi.setAdapter(adapter);

        } catch (SQLException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void criarPedido() {
        if ((editTextPedidoId.length() == 0) ||
                (spinnerCliente.getSelectedItemPosition() == 0)) {
            Toast.makeText(view.getContext(), "Dados inválidos", Toast.LENGTH_LONG).show();
            return;
        }

        pedido = new Pedido(
                Integer.parseInt(editTextPedidoId.getText().toString()),
                (Cliente) spinnerCliente.getSelectedItem(),
                LocalDate.now()
        );

        Toast.makeText(view.getContext(), "Novo pedido criado", Toast.LENGTH_LONG).show();

        desabilitarCampos();
    }

    private void adicionarItem() {
        if (pedido == null) {
            Toast.makeText(view.getContext(), "Nenhum pedido em andamento", Toast.LENGTH_LONG).show();
            return;
        }

        if ((spinnerGibi.getSelectedItemPosition() == 0) ||
                (editTextQuantidade.length() == 0)) {
            Toast.makeText(view.getContext(), "Dados inválidos", Toast.LENGTH_LONG).show();
            return;
        }

        pedido.adicionarItem(new ItemPedido(
                pedido.getId(),
                (Gibi) spinnerGibi.getSelectedItem(),
                Integer.parseInt(editTextQuantidade.getText().toString()))
        );

        Toast.makeText(view.getContext(), "Item adicionado", Toast.LENGTH_LONG).show();

        textViewTotalPedido.setText(String.format("R$ %.2f", pedido.getTotal()));

        limparCamposItem();
    }

    private void limparPedido() {
        if (pedido == null) {
            Toast.makeText(view.getContext(), "Nenhum pedido em andamento", Toast.LENGTH_LONG).show();
            return;
        }

        pedido = null;

        habilitarCampos();
        limparCamposPedido();
    }

    private void registrarPedido() {
        if (pedido == null) {
            Toast.makeText(view.getContext(), "Nenhum pedido em andamento", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            pedidoController.registrar(pedido);

            Toast.makeText(view.getContext(), "Pedido registrado com sucesso", Toast.LENGTH_LONG).show();

        } catch (SQLException | IllegalArgumentException e) {
            Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        limparPedido();
    }

    private void limparCamposItem() {
        spinnerGibi.setSelection(0);
        editTextQuantidade.setText("");
    }

    private void limparCamposPedido() {
        editTextPedidoId.setText("");
        spinnerCliente.setSelection(0);
        textViewTotalPedido.setText("");
    }

    private void desabilitarCampos() {
        editTextPedidoId.setEnabled(false);
        spinnerCliente.setEnabled(false);
    }

    private void habilitarCampos() {
        editTextPedidoId.setEnabled(true);
        spinnerCliente.setEnabled(true);
    }
}
