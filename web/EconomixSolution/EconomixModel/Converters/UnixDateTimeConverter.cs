using System.Text.Json;
using System.Text.Json.Serialization;

namespace EconomixModel.Converters;

public class UnixDateTimeConverter : JsonConverter<DateTime>
{
    public override DateTime Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
    {
        if (reader.TokenType == JsonTokenType.Number)
        {
            long milliseconds = reader.GetInt64();
            return DateTimeOffset.FromUnixTimeMilliseconds(milliseconds).DateTime;
        }

        if (reader.TokenType == JsonTokenType.String)
        {
            string? value = reader.GetString();
            if (DateTime.TryParse(value, out var date))
                return date;
        }

        return default;
    }

    public override void Write(Utf8JsonWriter writer, DateTime value, JsonSerializerOptions options)
    {
        writer.WriteStringValue(value.ToString("yyyy-MM-ddTHH:mm:ss"));
    }
}
