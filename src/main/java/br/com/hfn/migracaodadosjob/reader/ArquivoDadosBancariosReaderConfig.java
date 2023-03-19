package br.com.hfn.migracaodadosjob.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import br.com.hfn.migracaodadosjob.dominio.DadosBancarios;

@Configuration
public class ArquivoDadosBancariosReaderConfig {

	@Bean
	public FlatFileItemReader<DadosBancarios> arquivoDadosBancariosReader(){
		return new FlatFileItemReaderBuilder<DadosBancarios>()
				.name("arquivoPessoaReader")
				.resource(new FileSystemResource("files/dados_bancarios.csv"))
				.delimited()
				.names("pessoaId","agencia","conta","banco","id")
				.addComment("--") // Ignora linhas iniciadas em "--"
				.targetType(DadosBancarios.class)
				//.fieldSetMapper(fieldSetMapper())
				.build();
	}
	
}
