package org.ice1000.julia.lang

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.options.colors.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import icons.JuliaIcons
import org.ice1000.julia.lang.psi.JuliaTypes
import org.intellij.lang.annotations.Language

object JuliaHighlighter : SyntaxHighlighter {
	@JvmField val KEYWORD = TextAttributesKey.createTextAttributesKey("JULIA_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
	@JvmField val SYMBOL = TextAttributesKey.createTextAttributesKey("JULIA_SYMBOL", HighlighterColors.TEXT)
	@JvmField val NUMBER = TextAttributesKey.createTextAttributesKey("JULIA_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
	@JvmField val FLOAT_LIT = TextAttributesKey.createTextAttributesKey("JULIA_FLOAT_LIT", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
	@JvmField val STRING = TextAttributesKey.createTextAttributesKey("JULIA_STRING", DefaultLanguageHighlighterColors.STRING)
	@JvmField val STRING_TEMPLATE = TextAttributesKey.createTextAttributesKey("STRING_TEMPLATE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
	@JvmField val STRING_ESCAPE = TextAttributesKey.createTextAttributesKey("JULIA_STRING_ESCAPE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
	@JvmField val STRING_INTERPOLATE = TextAttributesKey.createTextAttributesKey("JULIA_STRING_INTERPOLATE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
	@JvmField val STRING_ESCAPE_INVALID = TextAttributesKey.createTextAttributesKey("JULIA_STRING_ESCAPE_INVALID", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE)
	@JvmField val CHAR = TextAttributesKey.createTextAttributesKey("JULIA_CHAR", DefaultLanguageHighlighterColors.STRING)
	@JvmField val CHAR_ESCAPE = TextAttributesKey.createTextAttributesKey("JULIA_CHAR_ESCAPE", DefaultLanguageHighlighterColors.VALID_STRING_ESCAPE)
	@JvmField val CHAR_ESCAPE_INVALID = TextAttributesKey.createTextAttributesKey("JULIA_CHAR_ESCAPE_INVALID", DefaultLanguageHighlighterColors.INVALID_STRING_ESCAPE)
	@JvmField val OPERATOR = TextAttributesKey.createTextAttributesKey("JULIA_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
	@JvmField val ASSIGNMENT_OPERATOR = TextAttributesKey.createTextAttributesKey("JULIA_ASSIGNMENT_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
	@JvmField val UNICODE_OPERATOR = TextAttributesKey.createTextAttributesKey("JULIA_UNICODE_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
	@JvmField val BRACKET = TextAttributesKey.createTextAttributesKey("JULIA_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
	@JvmField val B_BRACKET = TextAttributesKey.createTextAttributesKey("JULIA_BRACES", DefaultLanguageHighlighterColors.BRACES)
	@JvmField val M_BRACKET = TextAttributesKey.createTextAttributesKey("JULIA_BRACKET", DefaultLanguageHighlighterColors.BRACKETS)
	@JvmField val SEMICOLON = TextAttributesKey.createTextAttributesKey("JULIA_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
	@JvmField val COMMENT = TextAttributesKey.createTextAttributesKey("JULIA_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
	@JvmField val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey("JULIA_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
	@JvmField val TYPE_NAME = TextAttributesKey.createTextAttributesKey("JULIA_TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME)
	@JvmField val ABSTRACT_TYPE_NAME = TextAttributesKey.createTextAttributesKey("JULIA_ABSTRACT_TYPE_NAME", DefaultLanguageHighlighterColors.INTERFACE_NAME)
	@JvmField val PRIMITIVE_TYPE_NAME = TextAttributesKey.createTextAttributesKey("JULIA_PRIMITIVE_TYPE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME)
	@JvmField val MODULE_NAME = TextAttributesKey.createTextAttributesKey("JULIA_MODULE_NAME", DefaultLanguageHighlighterColors.CLASS_NAME)
	@JvmField val FUNCTION_NAME = TextAttributesKey.createTextAttributesKey("JULIA_FUNCTION_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
	@JvmField val MACRO_NAME = TextAttributesKey.createTextAttributesKey("JULIA_MACRO_NAME", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
	@JvmField val MACRO_REFERENCE = TextAttributesKey.createTextAttributesKey("JULIA_MACRO_REFERENCE", DefaultLanguageHighlighterColors.FUNCTION_CALL)

	@JvmField val KEYWORD_KEY = arrayOf(KEYWORD)
	@JvmField val MACRO_REFERENCE_KEY = arrayOf(MACRO_REFERENCE)
	@JvmField val STRING_KEY = arrayOf(STRING)
	@JvmField val STRING_VALID_KEY = arrayOf(STRING_TEMPLATE)
	@JvmField val STRING_INTERPOLATE_KEY = arrayOf(STRING_INTERPOLATE)
	@JvmField val CHAR_KEY = arrayOf(CHAR)
	@JvmField val NUMBER_KEY = arrayOf(NUMBER)
	@JvmField val FLOAT_LIT_KEY = arrayOf(FLOAT_LIT)
	@JvmField val OPERATOR_KEY = arrayOf(OPERATOR)
	@JvmField val ASSIGNMENT_OPERATOR_KEY = arrayOf(ASSIGNMENT_OPERATOR)
	@JvmField val UNICODE_OPERATOR_KEY = arrayOf(UNICODE_OPERATOR)
	@JvmField val BRACKETS_KEY = arrayOf(BRACKET)
	@JvmField val B_BRACKETS_KEY = arrayOf(B_BRACKET)
	@JvmField val M_BRACKETS_KEY = arrayOf(M_BRACKET)
	@JvmField val COMMENT_KEY = arrayOf(COMMENT)
	@JvmField val SEMICOLON_KEY = arrayOf(SEMICOLON)
	@JvmField val BLOCK_COMMENT_KEY = arrayOf(BLOCK_COMMENT)

	private val UNICODE_OPERATOR_LIST = listOf(
		JuliaTypes.MISC_ARROW_SYM,
		JuliaTypes.MISC_COMPARISON_SYM,
		JuliaTypes.MISC_PLUS_SYM,
		JuliaTypes.MISC_MULTIPLY_SYM,
		JuliaTypes.MISC_EXPONENT_SYM
	)

	private val ASSIGNMENT_OPERATOR_LIST = JuliaTokenType.ASSIGN_OPERATORS.types.asList()
	private val OPERATOR_LIST = JuliaTokenType.BINARY_OPERATORS.types.asList()

	private val KEYWORDS_LIST = listOf(
		JuliaTypes.END_KEYWORD,
		JuliaTypes.MODULE_KEYWORD,
		JuliaTypes.BAREMODULE_KEYWORD,
		JuliaTypes.BREAK_KEYWORD,
		JuliaTypes.CONTINUE_KEYWORD,
		JuliaTypes.EXPORT_KEYWORD,
		JuliaTypes.IMPORT_KEYWORD,
		JuliaTypes.IMPORTALL_KEYWORD,
		JuliaTypes.USING_KEYWORD,
		JuliaTypes.IF_KEYWORD,
		JuliaTypes.ELSEIF_KEYWORD,
		JuliaTypes.ELSE_KEYWORD,
		JuliaTypes.FOR_KEYWORD,
		JuliaTypes.WHILE_KEYWORD,
		JuliaTypes.IN_KEYWORD,
		JuliaTypes.RETURN_KEYWORD,
		JuliaTypes.TRY_KEYWORD,
		JuliaTypes.CATCH_KEYWORD,
		JuliaTypes.FINALLY_KEYWORD,
		JuliaTypes.FUNCTION_KEYWORD,
		JuliaTypes.TYPE_KEYWORD,
		JuliaTypes.ABSTRACT_KEYWORD,
		JuliaTypes.PRIMITIVE_KEYWORD,
		JuliaTypes.STRUCT_KEYWORD,
		JuliaTypes.TYPEALIAS_KEYWORD,
		JuliaTypes.IMMUTABLE_KEYWORD,
		JuliaTypes.MUTABLE_KEYWORD,
		JuliaTypes.TRUE_KEYWORD,
		JuliaTypes.FALSE_KEYWORD,
		JuliaTypes.QUOTE_KEYWORD,
		JuliaTypes.MACRO_KEYWORD,
		JuliaTypes.LOCAL_KEYWORD,
		JuliaTypes.GLOBAL_KEYWORD,
		JuliaTypes.CONST_KEYWORD,
		JuliaTypes.LET_KEYWORD,
		JuliaTypes.DO_KEYWORD,
		JuliaTypes.BEGIN_KEYWORD,
		JuliaTypes.UNION_KEYWORD,
		JuliaTypes.WHERE_KEYWORD
	)

	/** parentheses */
	private val BRACKETS = listOf(JuliaTypes.LEFT_BRACKET, JuliaTypes.RIGHT_BRACKET)
	/** braces */
	private val B_BRACKETS = listOf(JuliaTypes.LEFT_B_BRACKET, JuliaTypes.RIGHT_B_BRACKET)
	/** brackets */
	private val M_BRACKETS = listOf(JuliaTypes.LEFT_M_BRACKET, JuliaTypes.RIGHT_M_BRACKET)

	override fun getHighlightingLexer() = JuliaLexerAdapter()
	override fun getTokenHighlights(type: IElementType?): Array<TextAttributesKey> = when (type) {
		JuliaTypes.QUOTE_START,
		JuliaTypes.QUOTE_END,
		JuliaTypes.CMD_QUOTE_START,
		JuliaTypes.CMD_QUOTE_END,
		JuliaTypes.TRIPLE_QUOTE_START,
		JuliaTypes.TRIPLE_QUOTE_END,
		JuliaTypes.REGEX_START,
		JuliaTypes.REGEX_END,
		JuliaTypes.VERSION_START,
		JuliaTypes.VERSION_END,
		JuliaTypes.BYTE_ARRAY_START,
		JuliaTypes.BYTE_ARRAY_END,
		JuliaTypes.RAW_STR_START,
		JuliaTypes.RAW_STR_END,
		JuliaTypes.REGULAR_STRING_PART_LITERAL -> STRING_KEY
		JuliaTypes.STRING_INTERPOLATE_START,
		JuliaTypes.STRING_INTERPOLATE_END -> STRING_INTERPOLATE_KEY
		JuliaTypes.STRING_ESCAPE,
		JuliaTypes.STRING_UNICODE -> STRING_VALID_KEY
		JuliaTypes.CHAR_LITERAL -> CHAR_KEY
		JuliaTypes.LINE_COMMENT -> COMMENT_KEY
		JuliaTypes.BLOCK_COMMENT_BODY,
		JuliaTypes.BLOCK_COMMENT_END,
		JuliaTypes.BLOCK_COMMENT_START -> BLOCK_COMMENT_KEY
		JuliaTypes.INT_LITERAL,
		JuliaTypes.FLOAT_LITERAL -> NUMBER_KEY
		JuliaTypes.SEMICOLON_SYM -> SEMICOLON_KEY
		JuliaTypes.FLOAT_CONSTANT -> FLOAT_LIT_KEY
		JuliaTypes.MACRO_SYM -> MACRO_REFERENCE_KEY
		in BRACKETS -> BRACKETS_KEY
		in M_BRACKETS -> M_BRACKETS_KEY
		in B_BRACKETS -> B_BRACKETS_KEY
		in KEYWORDS_LIST -> KEYWORD_KEY
		in OPERATOR_LIST -> OPERATOR_KEY
		in ASSIGNMENT_OPERATOR_LIST -> ASSIGNMENT_OPERATOR_KEY
		in UNICODE_OPERATOR_LIST -> UNICODE_OPERATOR_KEY
		else -> emptyArray()
	}
}

class JuliaHighlighterFactory : SyntaxHighlighterFactory() {
	override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?) = JuliaHighlighter
}

class JuliaColorSettingsPage : ColorSettingsPage {
	private companion object DescriptorHolder {
		private val DESCRIPTORS = arrayOf(
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.comment"), JuliaHighlighter.COMMENT),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.block-comment"), JuliaHighlighter.BLOCK_COMMENT),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.keyword"), JuliaHighlighter.KEYWORD),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.num"), JuliaHighlighter.NUMBER),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.operator"), JuliaHighlighter.OPERATOR),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.operator-assign"), JuliaHighlighter.ASSIGNMENT_OPERATOR),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.operator-unicode"), JuliaHighlighter.UNICODE_OPERATOR),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.brackets"), JuliaHighlighter.BRACKET),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.brackets.b"), JuliaHighlighter.B_BRACKET),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.brackets.m"), JuliaHighlighter.M_BRACKET),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.num-float-lit"), JuliaHighlighter.FLOAT_LIT),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.string"), JuliaHighlighter.STRING),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.string-interpolate"), JuliaHighlighter.STRING_INTERPOLATE),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.string-escape"), JuliaHighlighter.STRING_ESCAPE),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.string-escape-invalid"), JuliaHighlighter.STRING_ESCAPE_INVALID),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.char"), JuliaHighlighter.CHAR),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.char-escape"), JuliaHighlighter.CHAR_ESCAPE),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.char-escape-invalid"), JuliaHighlighter.CHAR_ESCAPE_INVALID),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.function-name"), JuliaHighlighter.FUNCTION_NAME),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.module-name"), JuliaHighlighter.MODULE_NAME),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.macro-name"), JuliaHighlighter.MACRO_NAME),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.macro-name.ref"), JuliaHighlighter.MACRO_REFERENCE),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.abs-type-name"), JuliaHighlighter.ABSTRACT_TYPE_NAME),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.prim-type-name"), JuliaHighlighter.PRIMITIVE_TYPE_NAME),
			AttributesDescriptor(JuliaBundle.message("julia.highlighter.settings.type-name"), JuliaHighlighter.TYPE_NAME)
		)

		private val ADDITIONAL_DESCRIPTORS = mapOf(
			"functionName" to JuliaHighlighter.FUNCTION_NAME,
			"moduleName" to JuliaHighlighter.MODULE_NAME,
			"macroName" to JuliaHighlighter.MACRO_NAME,
			"stringEscapeInvalid" to JuliaHighlighter.STRING_ESCAPE_INVALID,
			"typeName" to JuliaHighlighter.TYPE_NAME,
			"charEscape" to JuliaHighlighter.CHAR_ESCAPE,
			"charEscapeInvalid" to JuliaHighlighter.CHAR_ESCAPE_INVALID,
			"abstractTypeName" to JuliaHighlighter.ABSTRACT_TYPE_NAME,
			"primitiveTypeName" to JuliaHighlighter.PRIMITIVE_TYPE_NAME
		)
	}

	override fun getHighlighter(): SyntaxHighlighter = JuliaHighlighter
	override fun getAdditionalHighlightingTagToDescriptorMap() = ADDITIONAL_DESCRIPTORS
	override fun getIcon() = JuliaIcons.JULIA_BIG_ICON
	override fun getAttributeDescriptors() = DESCRIPTORS
	override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
	override fun getDisplayName() = JuliaFileType.name
	@Language("Julia")
	override fun getDemoText() =
		"""#=
		      BLOCK COMMENT
		   =#
		   module <moduleName>ice1000</moduleName>
		   NaN32 # (Float32)
		   (1 + 3.2)::Float64
			 raw"$$$!", v"1.0", b"\xff"
		   IntOrString = Union{Int, AbstractString}
		   div(5, 2) # => 2 # for a truncated result, use div
		   @printf "%d is less than %f" 4.5 5.3
		   ismatch(r"1 \+ 2 = 3", "1 + 2 = $(1 + 2)")
		   [1, 2, 3][2:end] # => 2, index start from 1
		   try
		       println("Hello\nWorld '<stringEscapeInvalid>\x</stringEscapeInvalid>jb'" +
		         '<charEscapeInvalid>\x</charEscapeInvalid>' + '<charEscape>\a<charEscape>')
		       some_other_var # => Unresolved reference: some_other_var
		   catch exception
		       println(exception)
		   end
		   abstract type <abstractTypeName>Cat</abstractTypeName> <: Animals end
		   primitive type <primitiveTypeName>Bool</primitiveTypeName> <: Integer 8 end
		   type <typeName>Dog</typeName> <: Animals
		       age::Int64
		   end
		   macro <macroName>assert</macroName>(condition)
		       return :( ${JULIA_STRING_DOLLAR}ex ? nothing : throw(AssertionError($JULIA_STRING_DOLLAR{'$'}(string(ex)))) )
		   end
		   function <functionName>fib</functionName>(n)
		       return n ≤ 2 ? 1 : fib(n - 1) + fib(n - 2)
		   end
		   for (k, v) in Dict("dog"=>"mammal", "cat"=>"mammal", "mouse"=>"mammal")
		       println("${JULIA_STRING_DOLLAR}k is a ${JULIA_STRING_DOLLAR}v")
		   end
		   x = 0
		   while x ≤ 4
		       println(x)
		       x += 1
		   end
		   end""".trimIndent()
}
