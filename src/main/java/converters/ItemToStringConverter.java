package converters;

import domain.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ItemToStringConverter implements Converter<Item, String>{
	
	@Override
	public String convert(final Item folder) {
		String result;

		if (folder == null)
			result = null;
		else
			result = String.valueOf(folder.getId());

		return result;
	}

}
