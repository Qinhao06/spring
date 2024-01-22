package qh.springframework.factory.convert.core;

import java.util.Set;

public interface GenericConverter {

    Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, Class<?> targetType);


    final class ConvertiblePair {
        private final Class<?> sourceType;
        private final Class<?> targetType;

        public ConvertiblePair(Class<?> sourceType, Class<?> targetType) {
            this.sourceType = sourceType;
            this.targetType = targetType;
        }

        public Class<?> getSourceType() {
            return sourceType;
        }

        public Class<?> getTargetType() {
            return targetType;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if(obj == null || obj.getClass() != ConvertiblePair.class){
                return false;
            }
            ConvertiblePair other = (ConvertiblePair) obj;
            return sourceType.equals(other.sourceType) && targetType.equals(other.targetType);
        }

        @Override
        public int hashCode() {
            return sourceType.hashCode() * 31 + targetType.hashCode();
        }
    }

}
