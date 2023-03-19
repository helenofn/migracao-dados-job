package br.com.hfn.migracaodadosjob.step;


import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.hfn.migracaodadosjob.dominio.Pessoa;

@Configuration
public class MigrarPessoasStepConfig {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Step migrarPessoasStep(
			ItemReader<Pessoa> arquivoPessoaReader,
			ClassifierCompositeItemWriter<Pessoa> pessoaClassifierWriter,
			FlatFileItemWriter<Pessoa> arquivoPessoasInvalidasWriter) {
		return stepBuilderFactory
				.get("migrarPessoasStep")
				.<Pessoa, Pessoa>chunk(10000)
				.reader(arquivoPessoaReader)
				.writer(pessoaClassifierWriter)
				.stream(arquivoPessoasInvalidasWriter)
				.build();
				
	}
}
