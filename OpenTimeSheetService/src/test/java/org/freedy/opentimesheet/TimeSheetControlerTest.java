package org.freedy.opentimesheet;

import static org.mockito.Matchers.any;

import java.util.Arrays;
import java.util.List;

import javax.validation.ValidationException;

import org.freedy.opentimesheet.controller.TimeSheetController;
import org.freedy.opentimesheet.model.Colaborador;
import org.freeedy.opentimesheet.persistence.TimeSheetRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration;



/**
 * Controller trata as operações REST sobre a entidade da <code>Colaborador</code>.
 * 
 * @author <a href="mailto:eder@yaw.com.br">Eder Magalhães</a>
 */
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = Application.class, loader = SpringApplicationContextLoader.class)
public class TimeSheetControlerTest {


    @InjectMocks
    private TimeSheetController controller;
    
    @Mock
    private TimeSheetRepository repository;
    
    private Colaborador buildColaborador(String id, String nome, String descricao, 
            String categoria, Double preco, Integer qtde) {
        Colaborador m = new Colaborador();
        m.setId(id);
        m.setNome(nome);

        return m;
    }
    
    @Test
    public void testListAll() throws Exception {
        Mockito.when(repository.findAll())
            .then(a -> Arrays.asList(
                    buildColaborador("1", "Colaborador Teste 1", "teste", "Cat", 100.0, 14),
                    buildColaborador("2", "Colaborador Teste 2", "outro teste", "Cat", 230.0, 4)));
        List<Colaborador> Colaboradors = controller.list();
        Assert.assertFalse(Colaboradors.isEmpty());
    }
    
    @Test
    public void testFindById() throws Exception {
        String ColaboradorId = "10";
        Mockito.when(repository.findAll())
            .then(a -> Arrays.asList(
                buildColaborador(ColaboradorId, "Colaborador Teste 10", "teste", "Cat", 180.0, 12)));
        List<Colaborador> Colaboradors = controller.list();
        Assert.assertFalse(Colaboradors.isEmpty());
        Assert.assertEquals(Colaboradors.stream().findFirst().get().getId(), ColaboradorId);
    }
    
    @Test
    public void testCreate() throws Exception {
        String novoId = "20";
        Colaborador antes = buildColaborador(null, "Nova", "Nova Colaborador", "Nova Categoria", 570.2, 22);
        Mockito.when(repository.save((Colaborador)any()))
            .then(a -> buildColaborador(novoId, "Nova", "Nova Colaborador", "Nova Categoria", 570.2, 22));
        
        Colaborador depois = controller.create(antes);
        Assert.assertTrue(antes.getId() == null);
        Assert.assertTrue(depois.getId() != null);
    }
    
    @Test(expected=ValidationException.class)
    public void testCreateInvalid() throws Exception {
        Colaborador m = buildColaborador(null, "Invalida", null, null, null, null);
        Mockito.when(repository.save((Colaborador)any())).thenThrow(new ValidationException());
        controller.create(m);
    }
    
    @Test
    public void testUpdate() throws Exception {
        final Colaborador m = buildColaborador("30", "Qualquer", "Outra Colaborador", "Nova Categoria", 570.2, 22);
        String nomeAnterior = m.getNome();
        m.setNome("Qualquer outra");
        Mockito.when(repository.save((Colaborador)any())).then(a -> m);
        Colaborador atualizada = controller.update(m.getId(), m);
        
        Assert.assertNotEquals(nomeAnterior, atualizada.getNome());
    }
    
    @Test(expected=ValidationException.class)
    public void testUpdateInvalid() throws Exception {
        Colaborador m = buildColaborador("30", "Qualquer", "Outra Colaborador", "Nova Categoria", 570.2, 22);

        Mockito.when(repository.save((Colaborador)any())).thenThrow(new ValidationException());
        controller.create(m);
    }
    
    @Test
    public void testRemove() throws Exception {
        String toRemove = "2";
        controller.remove(toRemove);        
    }
    
    @Test
    public void testSearchByNome() throws Exception {
        List<Colaborador> Colaboradors = Arrays.asList(
                buildColaborador("7", "Coca-cola", "Zero", "Bebidas", 3.5, 40),
                buildColaborador("8", "Inspirion 15", "Dell Notebooks", "Eletronico", 2350.0, 2));
        
        String nome = "coca";
        
        Mockito.when(repository.findByCriteria(any(), any(), any(), any(), any(), any(), any()))
        .then(a -> Arrays.asList(Colaboradors.get(0)));
        
        List<Colaborador> resultado = controller.search(nome, null, null, null, null, null);
        Assert.assertTrue(resultado.size() == 1);
    }
    
    @Test
    public void testSearchByCategoria() throws Exception {
        List<Colaborador> Colaboradors = Arrays.asList(
                buildColaborador("7", "Coca-cola", "Zero", "Bebidas", 3.5, 40),
                buildColaborador("8", "Inspirion 15", "Dell Notebooks", "Eletronico", 2350.0, 2));
        
        String categoria = "note";
        
        Mockito.when(repository.findByCriteria(any(), any(), any(), any(), any(), any(), any()))
        .then(a -> Arrays.asList(Colaboradors.get(1)));
        
        List<Colaborador> resultado = controller.search(null, null, categoria, null, null, null);
        Assert.assertTrue(resultado.size() == 1);
    }
    
    @Test
    public void testSearchByPrecoDe() throws Exception {
        List<Colaborador> Colaboradors = Arrays.asList(
                buildColaborador("7", "Coca-cola", "Zero", "Bebidas", 3.5, 40),
                buildColaborador("8", "Inspirion 15", "Dell Notebooks", "Eletronico", 2350.0, 2));
        
        Double precoDe = 2000.0;
        
        Mockito.when(repository.findByCriteria(any(), any(), any(), any(), any(), any(), any()))
        .then(a -> Arrays.asList(Colaboradors.get(1)));
        
        List<Colaborador> resultado = controller.search(null, null, null, precoDe, null, null);
        Assert.assertTrue(resultado.size() == 1);
    }
    
}
