using System.Globalization;
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
            return DateTimeOffset.FromUnixTimeMilliseconds(milliseconds).LocalDateTime;
        }

        if (reader.TokenType == JsonTokenType.String)
        {
            string? value = reader.GetString();
            if (!string.IsNullOrEmpty(value))
            {
                // Strip Java ZonedDateTime suffixes like [UTC], [America/Lima], etc.
                var bracketIndex = value.IndexOf('[');
                if (bracketIndex > 0)
                    value = value.Substring(0, bracketIndex);

                if (DateTime.TryParse(value, CultureInfo.InvariantCulture,
                    DateTimeStyles.RoundtripKind, out var date))
                {
                    if (date.Kind == DateTimeKind.Unspecified)
                        return DateTime.SpecifyKind(date, DateTimeKind.Local);
                    return date.ToLocalTime();
                }
            }
        }

        return default;
    }

    public override void Write(Utf8JsonWriter writer, DateTime value, JsonSerializerOptions options)
    {
        writer.WriteStringValue(value.ToString("yyyy-MM-ddTHH:mm:ss.fffK"));
    }
}
